package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface BishopMoveFactory {
    Move createSimpleBishopnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    Move createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
