package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 */
public class CapturePawnMove extends AbstractPawnMove {

    public CapturePawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    public CapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        super(from, to, cardinal);
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
        return super.equals(obj) && obj instanceof CapturePawnMove;
    }
}
