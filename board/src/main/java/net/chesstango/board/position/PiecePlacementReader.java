/**
 * 
 */
package net.chesstango.board.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface PiecePlacementReader extends Iterable<PiecePositioned>, GetElementByIndex<PiecePositioned> {

	PiecePositioned getPosicion(Square square);

	Piece getPiece(Square square);

	boolean isEmtpy(Square square);

	Iterator<PiecePositioned> iterator(SquareIterator squareIterator);

	Iterator<PiecePositioned> iterator(long positions);
}