package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.ColorBoard;


/**
 * @author Mauricio Coria
 */
class SimplePawnMove extends AbstractPawnMove {

    public SimplePawnMove(PiecePositioned from, PiecePositioned to) {
        super(from, to);
    }

    public SimplePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        super(from, to, cardinal);
    }

    @Override
    public void executeMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    @Override
    public void undoMove(ColorBoard colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof SimplePawnMove;
    }
}
