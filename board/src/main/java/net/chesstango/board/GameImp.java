package net.chesstango.board;

import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.MirrorBuilder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class GameImp implements Game {
    private final ChessPosition chessPosition;
    private final GameState gameState;
    private final PositionAnalyzer analyzer;
    private final GameVisitorAcceptor gameVisitorAcceptor;

    public GameImp(ChessPosition chessPosition, GameState gameState, PositionAnalyzer analyzer, GameVisitorAcceptor gameVisitorAcceptor) {
        this.chessPosition = chessPosition;
        this.gameState = gameState;
        this.analyzer = analyzer;
        this.gameVisitorAcceptor = gameVisitorAcceptor;
        this.chessPosition.init();
        this.analyzer.threefoldRepetitionRule(true);
        this.analyzer.fiftyMovesRule(true);
        saveInitialFEN();
    }

    @Override
    public String getInitialFEN() {
        return gameState.getInitialFEN();
    }

    @Override
    public Game executeMove(Square from, Square to) {
        Move move = getMove(from, to);
        if (move != null) {
            return executeMove(move);
        } else {
            throw new RuntimeException(String.format("Invalid move: %s%s", from, to));
        }
    }

    @Override
    public Game executeMove(Square from, Square to, Piece promotionPiece) {
        Move move = getMove(from, to, promotionPiece);
        if (move != null) {
            return executeMove(move);
        } else {
            throw new RuntimeException(String.format("Invalid move: %s%s %s", from, to, promotionPiece));
        }
    }

    @Override
    public Game executeMove(Move move) {
        gameState.setSelectedMove(move);

        gameState.push();

        chessPosition.doMove(move);

        // NO LLAMAR a updateGameState
        // Si la posicion se encuentra en cache no es necesario calcular los movimientos posibles
        // this.analyzer.updateGameState();

        return this;
    }


    @Override
    public Game undoMove() {
        gameState.pop();

        Move lastMove = gameState.getSelectedMove();

        chessPosition.undoMove(lastMove);

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
    public MoveContainerReader getPossibleMoves() {
        return getState().getLegalMoves();
    }

    @Override
    public GameStatus getStatus() {
        return getState().getStatus();
    }

    @Override
    public GameState getState() {
        if (gameState.getStatus() == null) {
            this.analyzer.updateGameState();
        }
        return gameState;
    }

    @Override
    public ChessPositionReader getChessPosition() {
        return chessPosition;
    }
    
	@Override
    public void accept(GameVisitor visitor) {
        gameVisitorAcceptor.accept(visitor);
    }


    @Override
    public Game mirror() {
        MirrorBuilder<Game> mirrorBuilder = new MirrorBuilder<>(new GameBuilder());
        getChessPosition().constructChessPositionRepresentation(mirrorBuilder);
        return mirrorBuilder.getChessRepresentation();
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

}
