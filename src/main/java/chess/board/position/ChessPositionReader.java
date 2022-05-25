/**
 * 
 */
package chess.board.position;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.ChessPositionBuilder;
import chess.board.iterators.SquareIterator;

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

	Square getKingSquare(Color color);

	Square getEnPassantSquare();

	long getPositions(Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWithoutKing(Color color);

	Iterator<PiecePositioned> iteratorAllPieces();
	
	void constructBoardRepresentation(ChessPositionBuilder<?> builder);
}
