package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface KingCacheBoardReader {
    Square getKingSquare(Color color);

    Square getSquareKingWhiteCache();

    Square getSquareKingBlackCache();

    Square getKingSquare(Color color, BoardReader board);
}
