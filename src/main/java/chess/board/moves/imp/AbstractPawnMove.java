package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.position.imp.PositionState;

abstract class AbstractPawnMove extends AbstractMove {
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
