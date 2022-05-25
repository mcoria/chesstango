package chess.board.movesgenerators.legal.squarecapturers;

import chess.board.Color;
import chess.board.Square;


public interface SquareCapturer {
    boolean positionCaptured(Color color, Square square);
}
