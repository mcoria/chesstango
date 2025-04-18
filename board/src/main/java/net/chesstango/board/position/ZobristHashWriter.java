package net.chesstango.board.position;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ZobristHashWriter {
    void xorPosition(PiecePositioned piecePositioned);

    void xorTurn();

    void xorCastleWhiteKing();

    void xorCastleWhiteQueen();

    void xorCastleBlackKing();

    void xorCastleBlackQueen();

    void xorEnPassantSquare(Square enPassantSquare);

    void clearEnPassantSquare();
}
