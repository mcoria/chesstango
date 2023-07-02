package net.chesstango.evaluation.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameEvaluator;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractEvaluator implements GameEvaluator {
    public static int evaluateFinalStatus(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case CHECK:
            case NO_CHECK:
                throw new RuntimeException("Game is still in progress");
        }
        return evaluation;
    }

    protected int evaluateByMaterial(final Game game) {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);

        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.getRook(Color.WHITE));
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.getRook(Color.WHITE));
        evaluation += Long.bitCount(whitePositions & positionReader.getBishopPositions()) * getPieceValue(Piece.getRook(Color.WHITE));
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.getRook(Color.WHITE));
        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.getRook(Color.WHITE));

        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.getRook(Color.BLACK));
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.getRook(Color.BLACK));
        evaluation += Long.bitCount(blackPositions & positionReader.getBishopPositions()) * getPieceValue(Piece.getRook(Color.BLACK));
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.getRook(Color.BLACK));
        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.getRook(Color.BLACK));

        return evaluation;
    }

    abstract int getPieceValue(Piece piece);
}
