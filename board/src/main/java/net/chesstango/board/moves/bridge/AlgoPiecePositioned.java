package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PiecePlacementWriter;

/**
 * @author Mauricio Coria
 */
class AlgoPiecePositioned {
    protected void defaultFnDoMovePiecePlacement(PiecePositioned from, PiecePositioned to, PiecePlacementWriter piecePlacementWriter) {
        piecePlacementWriter.move(from, to);
    }

    protected void defaultFnUndoMovePiecePlacement(PiecePositioned from, PiecePositioned to, PiecePlacementWriter piecePlacementWriter) {
        piecePlacementWriter.setPosition(from);
        piecePlacementWriter.setPosition(to);
    }
}
