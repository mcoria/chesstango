/**
 * 
 */
package chess.board.position;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.SquareIterator;

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