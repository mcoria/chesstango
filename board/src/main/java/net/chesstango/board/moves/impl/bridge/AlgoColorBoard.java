package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.BitBoardWriter;

/**
 * @author Mauricio Coria
 */
class AlgoColorBoard {
    public void captureFnDoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter colorBoard) {
        colorBoard.removePosition(to);

        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    public void captureFnUndoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());

        colorBoard.addPosition(to);
    }

    public void defaultFnDoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    public void defaultFnUndoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }
}
