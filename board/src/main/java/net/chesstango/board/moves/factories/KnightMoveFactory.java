package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 */
public interface KnightMoveFactory {
    PseudoMove createSimpleKnightMove(PiecePositioned from, PiecePositioned to);

    PseudoMove createCaptureKnightMove(PiecePositioned from, PiecePositioned to);
}
