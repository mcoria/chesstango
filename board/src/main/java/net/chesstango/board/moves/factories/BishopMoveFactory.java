package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 *
 */
public interface BishopMoveFactory {
    PseudoMove createSimpleBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    PseudoMove createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
