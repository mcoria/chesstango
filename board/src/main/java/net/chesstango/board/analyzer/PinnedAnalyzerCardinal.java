package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.position.PositionReader;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
abstract class PinnedAnalyzerCardinal {
    protected final PositionReader positionReader;

    protected final Color color;
    protected final Piece queen;
    protected final Piece bishopOrRook;
    protected final Cardinal[] cardinals;

    protected abstract long getPossibleCapturerInCardinalDirection(Square square, Cardinal cardinal);

    PinnedAnalyzerCardinal(PositionReader positionReader, Color color, Cardinal[] cardinals, Piece bishopOrRook) {
        this.positionReader = positionReader;
        this.color = color;
        this.queen = Piece.getQueen(color);
        this.bishopOrRook = bishopOrRook;
        this.cardinals = cardinals;
    }

    public void pinnedSquares(Square squareKing, AnalyzerResult result) {
        List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals = result.getPinnedPositionCardinals();
        long pinnedPositions = result.getPinnedSquares();

        for (Cardinal cardinal : cardinals) {
            if (getPossibleCapturerInCardinalDirection(squareKing, cardinal) != 0) {
                pinnedPositions |= pinnedSquaresByDirection(squareKing, cardinal, pinnedPositionCardinals);
            }
        }

        result.setPinnedSquares(pinnedPositions);
    }

    protected long pinnedSquaresByDirection(Square squareKingOpponent, Cardinal cardinal, List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals) {
        PiecePositioned possiblePinned = null;

        Iterator<PiecePositioned> iterator = positionReader.iterator(new CardinalSquareIterator(squareKingOpponent, cardinal));

        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            Piece piece = destino.getPiece();

            if (piece != null) {
                if (possiblePinned == null) {
                    if (color.equals(piece.getColor())) {
                        return 0;
                    } else {
                        possiblePinned = destino;
                    }
                } else {
                    if (queen.equals(piece) || bishopOrRook.equals(piece)) {
                        // Confirmado, tenemos pinned
                        pinnedPositionCardinals.add(new AbstractMap.SimpleImmutableEntry<>(possiblePinned, cardinal));
                        return possiblePinned.getSquare().bitPosition();
                    }
                    return 0;
                }
            }
        }
        return 0;
    }
}
