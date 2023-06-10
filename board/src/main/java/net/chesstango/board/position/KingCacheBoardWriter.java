package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;

public interface KingCacheBoardWriter {
    void setKingSquare(Color color, Square square);
}
