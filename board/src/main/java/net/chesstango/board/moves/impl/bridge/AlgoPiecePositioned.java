package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.BoardWriter;

/**
 * @author Mauricio Coria
 */
class AlgoPiecePositioned {
    protected void defaultFnDoMovePiecePlacement(PiecePositioned from, PiecePositioned to, BoardWriter boardWriter) {
        boardWriter.move(from, to);
    }

    protected void defaultFnUndoMovePiecePlacement(PiecePositioned from, PiecePositioned to, BoardWriter boardWriter) {
        boardWriter.setPosition(from);
        boardWriter.setPosition(to);
    }
}
