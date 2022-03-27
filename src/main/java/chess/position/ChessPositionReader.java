/**
 * 
 */
package chess.position;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.builder.ChessPositionBuilder;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.iterators.square.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader {

	Color getTurnoActual();

	boolean isCastlingWhiteQueenAllowed();
	boolean isCastlingWhiteKingAllowed();

	boolean isCastlingBlackQueenAllowed();
	boolean isCastlingBlackKingAllowed();

	Square getEnPassantSquare();
	
	PiecePositioned getPosicion(Square square);
	
	Color getColor(Square square);
	
	Piece getPieza(Square square);

	Square getKingSquare(Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWhitoutKing(Color color);

	PiecePlacementIterator iterator(SquareIterator squareIterator);
	
	void constructBoardRepresentation(ChessPositionBuilder<?> builder);	
}
