package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Square;

public interface SquareCapturerByPiece {
    boolean positionCaptured(Square square);
}
