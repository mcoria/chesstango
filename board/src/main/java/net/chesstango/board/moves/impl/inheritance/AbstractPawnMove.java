package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.PositionState;

abstract class AbstractPawnMove extends AbstractMove {
    public AbstractPawnMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public AbstractPawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    protected void updatePositionStateBeforeRollTurn(PositionState positionState) {
        positionState.resetHalfMoveClock();
        positionState.setEnPassantSquare(null);
    }
}
