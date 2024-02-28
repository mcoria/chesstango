package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface KnightMoveFactory {
    Move createSimpleKnightMove(PiecePositioned from, PiecePositioned to);

    Move createCaptureKnightMove(PiecePositioned from, PiecePositioned to);
}
