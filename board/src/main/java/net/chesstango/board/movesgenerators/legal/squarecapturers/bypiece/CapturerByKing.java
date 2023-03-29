package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.bypiece.KingBitIterator;
import net.chesstango.board.position.BoardReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public class CapturerByKing implements SquareCapturerByPiece{

    private final BoardReader boardReader;
    private final Color color;
    private final Piece king;

    public CapturerByKing(BoardReader boardReader, Color color) {
        this.boardReader = boardReader;
        this.color = color;
        this.king = Piece.getKing(color);
    }

    @Override
    public boolean positionCaptured(Square square) {
        Iterator<PiecePositioned> iterator = new KingBitIterator<PiecePositioned>(boardReader, square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if (king.equals(destino.getPiece())) {
                return true;
            }
        }
        return false;
    }
}
