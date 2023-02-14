package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
class SimpleMove extends AbstractNotPawnMove {

    public SimpleMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    public SimpleMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        super(from, to, cardinal);
    }

    @Override
    public void executeMove(PositionState positionState) {
        super.executeMove(positionState);
        positionState.incrementHalfMoveClock();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof SimpleMove;
    }
}