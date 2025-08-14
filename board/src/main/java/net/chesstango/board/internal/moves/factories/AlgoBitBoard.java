package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.BitBoardWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoBitBoard {
    public void captureFnDoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.removePosition(to);

        bitBoardWriter.swapPositions(from.piece(), from.square(), to.square());
    }

    public void captureFnUndoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.piece(), to.square(), from.square());

        bitBoardWriter.addPosition(to);
    }

    public void defaultFnDoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.piece(), from.square(), to.square());
    }

    public void defaultFnUndoBitBoard(PiecePositioned from, PiecePositioned to, BitBoardWriter bitBoardWriter) {
        bitBoardWriter.swapPositions(from.piece(), to.square(), from.square());
    }
}
