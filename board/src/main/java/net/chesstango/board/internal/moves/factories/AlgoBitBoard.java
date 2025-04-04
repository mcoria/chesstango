package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.BitBoardWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoBitBoard {
    public void captureFnDoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.removePosition(to);

        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
    }

    public void captureFnUndoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());

        bitBoardWriter.addPosition(to);
    }

    public void defaultFnDoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), from.getSquare(), to.getSquare());
    }

    public void defaultFnUndoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.getPiece(), to.getSquare(), from.getSquare());
    }
}
