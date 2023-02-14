package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.PositionState;

public abstract class AbstractPawnMove extends AbstractMove {
    public AbstractPawnMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
        super(from, to, direction);
    }

    public AbstractPawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    @Override
    public void executeMove(PositionState positionState) {
        super.executeMove(positionState);
        positionState.resetHalfMoveClock();
    }
}
