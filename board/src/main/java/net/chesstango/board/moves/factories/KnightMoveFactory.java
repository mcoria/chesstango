package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 */
public interface KnightMoveFactory {
    MoveImp createSimpleKnightMove(PiecePositioned from, PiecePositioned to);

    MoveImp createCaptureKnightMove(PiecePositioned from, PiecePositioned to);
}
