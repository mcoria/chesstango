package net.chesstango.evaluation;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public interface GameEvaluator {
    int INFINITE_POSITIVE = Integer.MAX_VALUE;
    int INFINITE_NEGATIVE = -INFINITE_POSITIVE;
    int WHITE_LOST = INFINITE_NEGATIVE;
    int BLACK_WON = WHITE_LOST;
    int BLACK_LOST = INFINITE_POSITIVE;
    int WHITE_WON = BLACK_LOST;

    static int evaluateFinalStatus(final Game game) {
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

    default int evaluateByMaterial(final Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            evaluation += getPieceValue(game, piece);
        }
        return evaluation;
    }

    int getPieceValue(final Game game, Piece piece);

    int evaluate(final Game game);
}
