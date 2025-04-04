package net.chesstango.board.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.GetElementByIndex;
import net.chesstango.board.iterators.bysquare.SquareIterator;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface SquareBoardReader extends Iterable<PiecePositioned>, GetElementByIndex<PiecePositioned> {

	PiecePositioned getPosition(Square square);

	Piece getPiece(Square square);

	boolean isEmpty(Square square);

	Iterator<PiecePositioned> iterator(SquareIterator squareIterator);

	Iterator<PiecePositioned> iterator(long positions);
}