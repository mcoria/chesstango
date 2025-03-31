package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 *
 */
public interface RookMoveFactory {
    PseudoMove createSimpleRookMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    PseudoMove createCaptureRookMove(PiecePositioned form, PiecePositioned to, Cardinal cardinal);
}
