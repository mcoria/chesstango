package net.chesstango.ai.imp.smart.evaluation;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

public interface GameEvaluator {
    int INFINITE_POSITIVE = Integer.MAX_VALUE;
    int INFINITE_NEGATIVE = -INFINITE_POSITIVE;
    int WHITE_LOST = INFINITE_NEGATIVE;
    int BLACK_WON = WHITE_LOST;
    int BLACK_LOST = INFINITE_POSITIVE;
    int WHITE_WON = BLACK_LOST;

    static int evaluateByMaterial(Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getValue();
            evaluation += piece.getPieceValue();
        }
        return evaluation;
    }

    int evaluate(Game game);
}
