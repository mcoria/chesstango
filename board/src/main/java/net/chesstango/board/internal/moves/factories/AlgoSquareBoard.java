package net.chesstango.board.internal.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.SquareBoardWriter;

/**
 * @author Mauricio Coria
 */
public class AlgoSquareBoard {
    public void defaultFnDoMovePiecePlacement(PiecePositioned from, PiecePositioned to, SquareBoardWriter squareBoardWriter) {
        squareBoardWriter.move(from, to);
    }

    public void defaultFnUndoMovePiecePlacement(PiecePositioned from, PiecePositioned to, SquareBoardWriter squareBoardWriter) {
        squareBoardWriter.setPosition(from);
        squareBoardWriter.setPosition(to);
    }
}
