package chess.board.movesgenerators.legal.squarecapturers.bypiece;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.byposition.bypiece.KingBitIterator;
import chess.board.iterators.byposition.bypiece.KnightBitIterator;
import chess.board.position.PiecePlacementReader;

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
            if (king.equals(destino.getValue())) {
                return true;
            }
        }
        return false;
    }
}
