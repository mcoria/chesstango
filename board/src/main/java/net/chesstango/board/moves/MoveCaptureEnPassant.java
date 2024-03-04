package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;

/**
 * @author Mauricio Coria
 */
public interface MoveCaptureEnPassant extends Move {
    PiecePositioned getCapture();
}
