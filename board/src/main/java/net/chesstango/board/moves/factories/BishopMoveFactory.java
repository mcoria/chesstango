package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCommand;

/**
 * @author Mauricio Coria
 *
 */
public interface BishopMoveFactory {
    MoveCommand createSimpleBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    MoveCommand createCaptureBishopMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
