package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionStateReader {
    Square getEnPassantSquare();

    boolean isCastlingWhiteQueenAllowed();

    boolean isCastlingWhiteKingAllowed();

    boolean isCastlingBlackQueenAllowed();

    boolean isCastlingBlackKingAllowed();

    Color getCurrentTurn();

    int getHalfMoveClock();

    int getFullMoveClock();
}
