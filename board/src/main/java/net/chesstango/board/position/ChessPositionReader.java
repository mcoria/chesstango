/**
 * 
 */
package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.ChessRepresentationBuilder;
import net.chesstango.board.iterators.SquareIterator;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader extends PiecePlacementReader, PositionStateReader {

	Color getColor(Square square);

	Square getKingSquare(Color color);

	long getPositions(Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWithoutKing(Color color);

	Iterator<PiecePositioned> iteratorAllPieces();

	long getHash();

	void constructBoardRepresentation(ChessRepresentationBuilder<?> builder);

}
