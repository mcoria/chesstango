package chess.board.movesgenerators.legal.squarecapturers.bypiece;

import chess.board.Square;

public interface SquareCapturerByPiece {
    boolean positionCaptured(Square square);
}