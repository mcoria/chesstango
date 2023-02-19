package net.chesstango.board.movesgenerators.legal.squarecapturers.bypiece;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.bypiece.KingBitIterator;
import net.chesstango.board.position.PiecePlacementReader;

import java.util.Iterator;

public class CapturerByKing implements SquareCapturerByPiece{

    private final PiecePlacementReader piecePlacementReader;
    private final Color color;
    private final Piece king;

    public CapturerByKing(PiecePlacementReader piecePlacementReader, Color color) {
        this.piecePlacementReader = piecePlacementReader;
        this.color = color;
        this.king = Piece.getKing(color);
    }

    @Override
    public boolean positionCaptured(Square square) {
        Iterator<PiecePositioned> iterator = new KingBitIterator<PiecePositioned>(piecePlacementReader, square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if (king.equals(destino.getPiece())) {
                return true;
            }
        }
        return false;
    }
}
