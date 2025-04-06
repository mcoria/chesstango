package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionStateWriter {
    void setEnPassantSquare(Square enPassantSquare);

    void setCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

    void setCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

    void setCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

    void setCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);

    void setCurrentTurn(Color turn);

    void rollTurn();

    void setHalfMoveClock(int halfMoveClock);

    void incrementHalfMoveClock();

    void resetHalfMoveClock();

    void setFullMoveClock(int fullMoveClock);

    void incrementFullMoveClock();

    void pushState();

    void popState();
}
