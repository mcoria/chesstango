package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;

/**
 * Interface representing a move that captures en passant in a chess game.
 * This interface extends the Move interface and provides an additional method
 * to get the captured piece.
 *
 * @author Mauricio Coria
 */
public interface MoveCaptureEnPassant extends Move {
    /**
     * Gets the piece that is captured en passant.
     *
     * @return the captured piece
     */
    PiecePositioned getCapture();
}
