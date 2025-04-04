package net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.bypiece.KingBitIterator;
import net.chesstango.board.position.SquareBoardReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class CapturerByKing implements CapturerByPiece {

    private final SquareBoardReader squareBoardReader;
    private final Piece king;

    public CapturerByKing(SquareBoardReader squareBoardReader, Color color) {
        this.squareBoardReader = squareBoardReader;
        this.king = Piece.getKing(color);
    }

    @Override
    public boolean positionCaptured(Square square, long possibleThreats) {
        Iterator<PiecePositioned> iterator = new KingBitIterator<>(squareBoardReader, square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if (king.equals(destino.getPiece())) {
                return true;
            }
        }
        return false;
    }
}
