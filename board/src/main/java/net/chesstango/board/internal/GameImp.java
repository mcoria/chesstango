package net.chesstango.board.internal;

import lombok.Setter;
import net.chesstango.board.*;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.MirrorPositionBuilder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.PseudoMove;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorByPieceResult;
import net.chesstango.board.position.*;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the Game interface.
 * This class represents a chess game and provides methods to execute and undo moves,
 * retrieve the current and initial FEN (Forsyth-Edwards Notation) representation of the game,
 * and manage game listeners.
 * It also supports rules like threefold repetition and fifty-move rule.
 * <p>
 * The class uses a ChessPosition to represent the current state of the chessboard,
 * and a GameState to manage the state of the game.
 * <p>
 * It also allows for the generation of pseudo-moves and legal moves.
 * <p>
 * The class is designed to notify listeners of moves being executed or undone.
 * <p>
 * This class implements the Facade design pattern to provide a simplified interface
 * to the complex subsystem of chess game management, including move execution, state management,
 * and rule enforcement.
 * <p>
 * Dependencies:
 * - ChessPosition
 * - GameState
 * - MoveGenerator
 * - PositionAnalyzer
 * - FENEncoder
 * - GameListener
 * - Move
 * - PseudoMove
 * - MoveContainer
 * - MoveContainerReader
 * - MirrorPositionBuilder
 * - GameBuilder
 *
 * @author Mauricio Coria
 */
public class GameImp implements Game {
    private final Position position;

    private final GameState gameState;

    private final GameHistory gameHistory;

    private final List<GameListener> gameListeners = new ArrayList<>();

    private PositionAnalyzer analyzer;

    @Setter
    private MoveGenerator pseudoMovesGenerator;

    public GameImp(Position position, GameState gameState, GameHistory gameHistory) {
        this.position = position;
        this.gameState = gameState;
        this.gameHistory = gameHistory;
        this.position.init();
        saveInitialFEN();
    }

    @Override
    public FEN getInitialFEN() {
        return gameState.getInitialFEN();
    }

    @Override
    public FEN getCurrentFEN() {
        FENBuilder encoder = new FENBuilder();
        getPosition().constructChessPositionRepresentation(encoder);
        return encoder.getPositionRepresentation();
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
        GameHistoryRecord previousState = gameHistory.peekLastRecord();

        Move lasMove = previousState.playedMove();

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
        this.analyzer.setThreefoldRepetitionRule(flag);
    }

    @Override
    public void fiftyMovesRule(boolean flag) {
        this.analyzer.setFiftyMovesRule(flag);
    }

    @Override
    public MoveContainerReader<Move> getPossibleMoves() {
        return getState().getLegalMoves();
    }

    @Override
    public MoveContainerReader<PseudoMove> getPseudoMoves() {
        MoveContainer<PseudoMove> pseudoMoves = new MoveContainer<>();

        Iterator<PiecePositioned> iteratorAllPieces = position.iteratorAllPieces();

        while (iteratorAllPieces.hasNext()) {
            PiecePositioned piecePositioned = iteratorAllPieces.next();

            MoveGeneratorByPieceResult generationResult = pseudoMovesGenerator.generateByPiecePseudoMoves(piecePositioned);

            pseudoMoves.add(generationResult.getPseudoMoves());
        }

        //TODO: el problema es que solo generamos Castling moves para el turno actual cuando deberiamos generar para ambos
        //pseudoMoves.add(pseudoMovesGenerator.generateEnPassantPseudoMoves());
        //pseudoMoves.add(pseudoMovesGenerator.generateCastlingPseudoMoves());

        return pseudoMoves;
    }

    @Override
    public void addGameListener(GameListener gameListener) {
        gameListeners.add(gameListener);
    }

    @Override
    public Status getStatus() {
        return getState().getStatus();
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    @Override
    public GameHistory getHistory() {
        return gameHistory;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Game mirror() {
        MirrorPositionBuilder<Game> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new GameBuilder());
        getPosition().constructChessPositionRepresentation(mirrorChessPositionBuilder);
        return mirrorChessPositionBuilder.getPositionRepresentation();
    }

    @Override
    public String toString() {
        return position.toString();
    }

    private void saveInitialFEN() {
        FENBuilder encoder = new FENBuilder();

        position.constructChessPositionRepresentation(encoder);

        gameState.setInitialFEN(encoder.getPositionRepresentation());
    }

    public void setAnalyzer(PositionAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.analyzer.setThreefoldRepetitionRule(true);
        this.analyzer.setFiftyMovesRule(true);
        this.analyzer.updateGameState();
        this.gameListeners.add(this.analyzer);
    }

    public void notifyDoMove(Move move) {
        if (!gameListeners.isEmpty()) {
            for (GameListener gameListener : gameListeners) {
                gameListener.notifyDoMove(move);
            }
        }
    }

    public void notifyUndoMove(Move move) {
        if (!gameListeners.isEmpty()) {
            for (GameListener gameListener : gameListeners.reversed()) {
                gameListener.notifyUndoMove(move);
            }
        }
    }
}
