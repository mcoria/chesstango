package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.bypiece.KnightBitIterator;
import net.chesstango.board.position.SquareBoardReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class CapturerByKnight implements CapturerByPiece {

    private final SquareBoardReader squareBoardReader;
    private final Color color;
    private final Piece knight;

    public CapturerByKnight(SquareBoardReader squareBoardReader, Color color) {
        this.squareBoardReader = squareBoardReader;
        this.color = color;
        this.knight = Piece.getKnight(color);
    }

    @Override
    public boolean positionCaptured(Square square) {
        Iterator<PiecePositioned> iterator = new KnightBitIterator<PiecePositioned>(squareBoardReader, square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if (knight.equals(destino.getPiece())) {
                return true;
            }
        }
        return false;
    }
}
