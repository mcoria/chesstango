package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.bypiece.PawnBlackBitIterator;
import net.chesstango.board.iterators.byposition.bypiece.PawnWhiteBitIterator;
import net.chesstango.board.position.BoardReader;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class CapturerByPawn implements CapturerByPiece {

    private final BoardReader boardReader;
    private final Color color;
    private final Piece pawn;
    private final Function<Square, Iterator<PiecePositioned>> createPawnJumpsIterator;

    public CapturerByPawn(BoardReader boardReader, Color color) {
        this.boardReader = boardReader;
        this.color = color;
        this.pawn = Piece.getPawn(color);
        this.createPawnJumpsIterator = Color.WHITE.equals(color) ? this::createPawnWhiteIterator : this::createPawnBlackIterator;
    }

    @Override
    public boolean positionCaptured(Square square) {
        Iterator<PiecePositioned> iterator = createPawnJumpsIterator.apply(square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if (pawn.equals(destino.getPiece())) {
                return true;
            }
        }
        return false;
    }

    private Iterator<PiecePositioned> createPawnWhiteIterator(Square square) {
        return new PawnWhiteBitIterator<PiecePositioned>(boardReader, square);
    }

    private Iterator<PiecePositioned> createPawnBlackIterator(Square square) {
        return new PawnBlackBitIterator<PiecePositioned>(boardReader, square);
    }
}
