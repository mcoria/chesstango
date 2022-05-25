package chess.board.movesgenerators.legal.squarecapturers.bypiece;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.byposition.bypiece.KnightBitIterator;
import chess.board.position.PiecePlacementReader;

import java.util.Iterator;

public class CapturerByKnight implements SquareCapturerByPiece{

    private final PiecePlacementReader piecePlacementReader;
    private final Color color;
    private final Piece knight;

    public CapturerByKnight(PiecePlacementReader piecePlacementReader, Color color) {
        this.piecePlacementReader = piecePlacementReader;
        this.color = color;
        this.knight = Piece.getKnight(color);
    }

    @Override
    public boolean positionCaptured(Square square) {
        Iterator<PiecePositioned> iterator = new KnightBitIterator<PiecePositioned>(piecePlacementReader, square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if(knight.equals(destino.getValue())){
                return true;
            }
        }
        return false;
    }
}
