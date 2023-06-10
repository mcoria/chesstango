package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ColorBoard;
import net.chesstango.board.position.ColorBoardWriter;

/**
 * @author Mauricio Coria
 */
class AlgoColorBoard {
    public void captureFnDoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoardWriter colorBoard) {
        colorBoard.removePositions(to);

        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    public void captureFnUndoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());

        colorBoard.addPositions(to);
    }

    public void defaultFnDoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
    }

    public void defaultFnUndoColorBoard(PiecePositioned from, PiecePositioned to, ColorBoardWriter colorBoard) {
        colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
    }
}
