package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface SquareCapturerByPiece {
    boolean positionCaptured(Square square);
}
