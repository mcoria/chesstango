package net.chesstango.board.internal.moves.generators.legal.squarecapturers.bypiece;

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
    private final Piece knight;

    public CapturerByKnight(SquareBoardReader squareBoardReader, Color color) {
        this.squareBoardReader = squareBoardReader;
        this.knight = Piece.getKnight(color);
    }

    @Override
    public boolean positionCaptured(Square square, long possibleThreats) {
        Iterator<PiecePositioned> iterator = new KnightBitIterator<>(squareBoardReader, square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if (knight.equals(destino.piece())) {
                return true;
            }
        }
        return false;
    }
}
