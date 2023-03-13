package net.chesstango.board;

import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class GameImp implements Game {
    private final ChessPosition chessPosition;
    private final GameState gameState;
    private final PositionAnalyzer analyzer;
    private final Map<Class, Object> objectMap;
    private boolean detectRepetitions;

    public GameImp(ChessPosition chessPosition, GameState gameState, PositionAnalyzer analyzer, Map<Class, Object> objectMap) {
        this.chessPosition = chessPosition;
        this.gameState = gameState;
        this.analyzer = analyzer;
        this.objectMap = objectMap;

        this.chessPosition.init();
        this.analyzer.updateGameState();

        saveFEN();
    }

    @Override
    public Game executeMove(Square from, Square to) {
        Move move = getMove(from, to);
        if (move != null) {
            return executeMove(move);
        } else {
            throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
        }
    }

    @Override
    public Game executeMove(Move move) {
        gameState.setSelectedMove(move);

        gameState.push();

        chessPosition.acceptForExecute(move);

        if (detectRepetitions) {
            saveFENWithoutClocks();
        }

        analyzer.updateGameState();

        return this;
    }


    @Override
    public Game undoMove() {
        gameState.pop();

        Move lastMove = gameState.getSelectedMove();

        chessPosition.acceptForUndo(lastMove);

        return this;
    }

    @Override
    public Move getMove(Square from, Square to) {
        for (Move move : getPossibleMoves()) {
            if (from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare())) {
                return move;
            }
        }
        return null;
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        for (Move move : getPossibleMoves()) {
            if (from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare()) && (move instanceof MovePromotion)) {
                MovePromotion movePromotion = (MovePromotion) move;
                if (movePromotion.getPromotion().equals(promotionPiece)) {
                    return move;
                }
            }
        }
        return null;
    }

    @Override
    public void detectRepetitions(boolean flag) {
        this.detectRepetitions = flag;
        this.analyzer.detectRepetitions(flag);
        if (detectRepetitions) {
            saveFENWithoutClocks();
        }
    }

    @Override
    public MoveContainerReader getPossibleMoves() {
        return gameState.getLegalMoves();
    }

    @Override
    public GameStatus getStatus() {
        return gameState.getStatus();
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    @Override
    public ChessPositionReader getChessPosition() {
        return chessPosition;
    }

    @Override
    public <V extends GameVisitor> V accept(V gameVisitor) {
        gameState.accept(gameVisitor);
        return gameVisitor;
    }

    public <T> T getObject(Class<T> theClass) {
        return (T) objectMap.get(theClass);
    }

    @Override
    public String toString() {
        return chessPosition.toString();
    }

    private void saveFEN() {
        FENEncoder encoder = new FENEncoder();

        chessPosition.constructBoardRepresentation(encoder);

        gameState.setInitialFEN(encoder.getChessRepresentation());
    }

    private void saveFENWithoutClocks() {
        FENEncoder encoder = new FENEncoder();

        chessPosition.constructBoardRepresentation(encoder);

        gameState.setFenWithoutClocks(encoder.getFENWithoutClocks());
    }
}
