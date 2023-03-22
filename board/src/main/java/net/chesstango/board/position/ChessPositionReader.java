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
public interface ChessPositionReader extends PiecePlacementReader {

	Color getCurrentTurn();

	Color getColor(Square square);

	boolean isCastlingWhiteQueenAllowed();
	boolean isCastlingWhiteKingAllowed();

	boolean isCastlingBlackQueenAllowed();
	boolean isCastlingBlackKingAllowed();

	int getHalfMoveClock();

	int getFullMoveClock();

	Square getKingSquare(Color color);

	Square getEnPassantSquare();

	long getPositions(Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWithoutKing(Color color);

	Iterator<PiecePositioned> iteratorAllPieces();

	long getHash();

	void constructBoardRepresentation(ChessRepresentationBuilder<?> builder);

}
