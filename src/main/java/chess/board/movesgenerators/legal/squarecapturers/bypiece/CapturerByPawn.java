package chess.board.movesgenerators.legal.squarecapturers.bypiece;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.byposition.bypiece.KingBitIterator;
import chess.board.iterators.byposition.bypiece.PawnBlackBitIterator;
import chess.board.iterators.byposition.bypiece.PawnWhiteBitIterator;
import chess.board.position.PiecePlacementReader;

import java.util.Iterator;
import java.util.function.Function;

public class CapturerByPawn implements SquareCapturerByPiece{

    private final PiecePlacementReader piecePlacementReader;
    private final Color color;
    private final Piece pawn;
    private final Function<Square, Iterator<PiecePositioned>> createPawnJumpsIterator;

    public CapturerByPawn(PiecePlacementReader piecePlacementReader, Color color) {
        this.piecePlacementReader = piecePlacementReader;
        this.color = color;
        this.pawn = Piece.getPawn(color);
        this.createPawnJumpsIterator = Color.WHITE.equals(color) ? this::createPawnWhiteIterator : this::createPawnBlackIterator;
    }

    @Override
    public boolean positionCaptured(Square square) {
        Iterator<PiecePositioned> iterator = createPawnJumpsIterator.apply(square);
        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            if(pawn.equals(destino.getValue())){
                return true;
            }
        }
        return false;
    }

    private Iterator<PiecePositioned> createPawnWhiteIterator(Square square) {
        return new PawnWhiteBitIterator<PiecePositioned>(piecePlacementReader, square);
    }

    private Iterator<PiecePositioned> createPawnBlackIterator(Square square) {
        return new PawnBlackBitIterator<PiecePositioned>(piecePlacementReader, square);
    }
}
