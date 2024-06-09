package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 *
 */
public interface BishopMoveFactory {
    MoveImp createSimpleBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    MoveImp createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
