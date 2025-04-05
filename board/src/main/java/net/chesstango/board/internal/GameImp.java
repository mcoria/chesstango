package net.chesstango.board.internal;

import net.chesstango.board.*;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.MirrorPositionBuilder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.GameState;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GameImp implements Game {
    private final ChessPosition chessPosition;
    private final GameState gameState;
    private final GameVisitorAcceptor gameVisitorAcceptor;

    private final List<GameListener> gameListenerList;

    private PositionAnalyzer analyzer;

    public GameImp(ChessPosition chessPosition, GameState gameState, GameVisitorAcceptor gameVisitorAcceptor) {
        this.chessPosition = chessPosition;
        this.gameState = gameState;
        this.gameVisitorAcceptor = gameVisitorAcceptor;
        this.gameListenerList = new ArrayList<>();
        this.chessPosition.init();
        saveInitialFEN();
    }

    @Override
    public FEN getInitialFEN() {
        return gameState.getInitialFEN();
    }

    @Override
    public FEN getCurrentFEN() {
        FENEncoder encoder = new FENEncoder();
        getChessPosition().constructChessPositionRepresentation(encoder);
        return encoder.getChessRepresentation();
    }

    @Override
    public Game executeMove(Square from, Square to) {
        Move move = getMove(from, to);
        if (move != null) {
            move.executeMove();
        } else {
            throw new RuntimeException(String.format("Invalid move: %s%s", from, to));
        }
        return this;
    }

    @Override
    public Game executeMove(Square from, Square to, Piece promotionPiece) {
        Move move = getMove(from, to, promotionPiece);
        if (move != null) {
            move.executeMove();
        } else {
            throw new RuntimeException(String.format("Invalid move: %s%s %s", from, to, promotionPiece));
        }
        return this;
    }

    @Override
    public Game undoMove() {
        Move lasMove = gameState.getPreviousState().getSelectedMove();

        lasMove.undoMove();

        return this;
    }

    @Override
    public Move getMove(Square from, Square to) {
        return getState().getLegalMoves().getMove(from, to);
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        return getState().getLegalMoves().getMove(from, to, promotionPiece);
    }

    @Override
    public void threefoldRepetitionRule(boolean flag) {
        this.analyzer.threefoldRepetitionRule(flag);
    }

    @Override
    public void fiftyMovesRule(boolean flag) {
        this.analyzer.fiftyMovesRule(flag);
    }

    @Override
    public MoveContainerReader<Move> getPossibleMoves() {
        return getState().getLegalMoves();
    }

    @Override
    public void addGameListener(GameListener gameListener) {
        gameListenerList.add(gameListener);
    }

    @Override
    public GameStatus getStatus() {
        return getState().getStatus();
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    @Override
    public ChessPosition getChessPosition() {
        return chessPosition;
    }

    @Override
    public void accept(GameVisitor visitor) {
        gameVisitorAcceptor.accept(visitor);
    }


    @Override
    public Game mirror() {
        MirrorPositionBuilder<Game> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new GameBuilder());
        getChessPosition().constructChessPositionRepresentation(mirrorChessPositionBuilder);
        return mirrorChessPositionBuilder.getChessRepresentation();
    }

    @Override
    public String toString() {
        return chessPosition.toString();
    }

    private void saveInitialFEN() {
        FENEncoder encoder = new FENEncoder();

        chessPosition.constructChessPositionRepresentation(encoder);

        gameState.setInitialFEN(encoder.getChessRepresentation());
    }

    public void setAnalyzer(PositionAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.analyzer.threefoldRepetitionRule(true);
        this.analyzer.fiftyMovesRule(true);
        this.analyzer.updateGameState();
        this.gameListenerList.add(this.analyzer);
    }

    public void notifyDoMove(Move move) {
        if (!gameListenerList.isEmpty()) {
            for (GameListener gameListener : gameListenerList) {
                gameListener.notifyDoMove(move);
            }
        }
    }

    public void notifyUndoMove(Move move) {
        if (!gameListenerList.isEmpty()) {
            for (GameListener gameListener : gameListenerList) {
                gameListener.notifyUndoMove(move);
            }
        }
    }
}
