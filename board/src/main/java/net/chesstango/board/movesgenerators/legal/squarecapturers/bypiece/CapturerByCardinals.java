package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.position.BoardReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public abstract class CapturerByCardinals implements CapturerByPiece {
    private final BoardReader boardReader;
    private final Piece bishopOrRook;
    private final Piece queen;
    private final Cardinal[] cardinals;

    public CapturerByCardinals(BoardReader boardReader, Color color, Cardinal[] cardinals, Piece bishopOrRook) {
        this.boardReader = boardReader;
        this.cardinals = cardinals;
        this.bishopOrRook = bishopOrRook;
        this.queen = Piece.getQueen(color);
    }

    @Override
    public boolean positionCaptured(Square square) {
        for (Cardinal cardinal : cardinals) {
            if (positionCapturedByCardinal(square, cardinal)) {
                return true;
            }
        }
        return false;
    }


    private boolean positionCapturedByCardinal(Square square, Cardinal cardinal) {
        Iterator<PiecePositioned> iterator = boardReader.iterator(new CardinalSquareIterator(square, cardinal));
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            Piece piece = destino.getPiece();
            if (piece == null) {
                continue;
            } else if (queen.equals(piece)) {
                return true;
            } else if (bishopOrRook.equals(piece)) {
                return true;
            } else {
                break;
            }
        }
        return false;
    }
}
