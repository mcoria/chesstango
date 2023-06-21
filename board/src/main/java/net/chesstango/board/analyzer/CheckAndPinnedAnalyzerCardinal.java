package net.chesstango.board.analyzer;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.iterators.bysquare.CardinalSquareIterator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
abstract class CheckAndPinnedAnalyzerCardinal {
    protected final ChessPositionReader positionReader;

    protected final Color color;
    protected final Piece queen;
    protected final Piece bishopOrRook;
    protected final Cardinal[] cardinals;

    protected long pinnedPositions = 0;

    protected abstract long getPossibleCapturerInCardinalDirection(Square square, Cardinal cardinal);

    CheckAndPinnedAnalyzerCardinal(ChessPositionReader positionReader, Color color, Cardinal[] cardinals, Piece bishopOrRook) {
        this.positionReader = positionReader;
        this.color = color;
        this.queen = Piece.getQueen(color);
        this.bishopOrRook = bishopOrRook;
        this.cardinals = cardinals;
    }

    public boolean positionCaptured(Square squareKingOpponent, long positionsAttackers, List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals) {
        pinnedPositions = 0;
        for (Cardinal cardinal : cardinals) {
            if ((getPossibleCapturerInCardinalDirection(squareKingOpponent, cardinal) & positionsAttackers) != 0) {
                if (positionCapturedByDirection(squareKingOpponent, cardinal, pinnedPositionCardinals)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean positionCapturedByDirection(Square squareKingOpponent, Cardinal cardinal, List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals) {
        PiecePositioned possiblePinned = null;

        Iterator<PiecePositioned> iterator = positionReader.iterator(new CardinalSquareIterator(squareKingOpponent, cardinal));

        while (iterator.hasNext()) {
            PiecePositioned destino = iterator.next();
            Piece piece = destino.getPiece();

            if (piece != null) {
                if (possiblePinned == null) {
                    if (color.equals(piece.getColor())) {
                        if (queen.equals(piece) || bishopOrRook.equals(piece)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        possiblePinned = destino;
                    }

                } else {
                    if (queen.equals(piece) || bishopOrRook.equals(piece)) {
                        // Confirmado, tenemos pinned
                        pinnedPositions |= possiblePinned.getSquare().getBitPosition();
                        pinnedPositionCardinals.add(new AbstractMap.SimpleImmutableEntry<>(possiblePinned, cardinal));
                    }
                    return false;
                }
            }
        }
        return false;
    }
}
