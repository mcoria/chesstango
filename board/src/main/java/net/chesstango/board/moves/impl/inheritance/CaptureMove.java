package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
class CaptureMove extends AbstractNotPawnMove {

    public CaptureMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    public CaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        super(from, to, cardinal);
    }

    @Override
    protected void updatePositionStateBeforeRollTurn(PositionState positionState) {
        super.updatePositionStateBeforeRollTurn(positionState);
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
    public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CaptureMove;
    }
}
