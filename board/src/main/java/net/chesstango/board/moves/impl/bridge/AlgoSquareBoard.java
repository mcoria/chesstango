package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.SquareBoardWriter;

/**
 * @author Mauricio Coria
 */
class AlgoSquareBoard {
    protected void defaultFnDoMovePiecePlacement(PiecePositioned from, PiecePositioned to, SquareBoardWriter squareBoardWriter) {
        squareBoardWriter.move(from, to);
    }

    protected void defaultFnUndoMovePiecePlacement(PiecePositioned from, PiecePositioned to, SquareBoardWriter squareBoardWriter) {
        squareBoardWriter.setPosition(from);
        squareBoardWriter.setPosition(to);
    }
}
