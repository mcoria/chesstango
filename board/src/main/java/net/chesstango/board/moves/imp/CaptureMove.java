package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
public class CaptureMove extends AbstractNotPawnMove {

    public CaptureMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    public CaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        super(from, to, cardinal);
    }

    @Override
    public void executeMove(PositionState positionState) {
        super.executeMove(positionState);
        positionState.resetHalfMoveClock();
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        colorBoard.removePositions(to);

        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());

        colorBoard.addPositions(to);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CaptureMove;
    }
}
