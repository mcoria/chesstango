package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;


public interface SquareCapturer {
    boolean positionCaptured(Color color, Square square);
}
