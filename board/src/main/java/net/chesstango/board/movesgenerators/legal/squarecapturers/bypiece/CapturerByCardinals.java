package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.position.BitBoardReader;
import net.chesstango.board.position.SquareBoardReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public abstract class CapturerByCardinals implements CapturerByPiece {
    protected final SquareBoardReader squareBoardReader;
    protected final BitBoardReader bitBoardReader;
    protected final Piece bishopOrRook;
    protected final Piece queen;
    protected final Cardinal[] cardinals;
    protected final Color color;

    protected abstract long getAttackerInCardinalDirection(Square square, Cardinal cardinal);

    public CapturerByCardinals(SquareBoardReader squareBoardReader, BitBoardReader bitBoardReader, Color color, Cardinal[] cardinals, Piece bishopOrRook) {
        this.squareBoardReader = squareBoardReader;
        this.bitBoardReader = bitBoardReader;
        this.cardinals = cardinals;
        this.bishopOrRook = bishopOrRook;
        this.queen = Piece.getQueen(color);
        this.color = color;
    }

    @Override
    public boolean positionCaptured(Square square, long possibleAttackers) {
        for (Cardinal cardinal : cardinals) {
            long attackerInCardinalDirection = getAttackerInCardinalDirection(square, cardinal);
            if ((attackerInCardinalDirection & possibleAttackers) != 0 && positionCapturedByCardinal(square, cardinal)) {
                return true;
            }
        }
        return false;
    }


    private boolean positionCapturedByCardinal(Square square, Cardinal cardinal) {
        Iterator<PiecePositioned> iterator = squareBoardReader.iterator(new CardinalSquareIterator(square, cardinal));
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
