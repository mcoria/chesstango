package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.BitBoardWriter;

/**
 * @author Mauricio Coria
 */
class AlgoColorBoard {
    public void captureFnDoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.removePosition(to);

        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
    }

    public void captureFnUndoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());

        bitBoardWriter.addPosition(to);
    }

    public void defaultFnDoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
    }

    public void defaultFnUndoColorBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());
    }
}
