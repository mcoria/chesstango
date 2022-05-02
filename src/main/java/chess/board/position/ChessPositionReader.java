/**
 * 
 */
package chess.board.position;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.ChessPositionBuilder;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.SquareIterator;

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

	Square getKingSquare(Color color);

	Square getEnPassantSquare();
	
	PiecePositioned getPosicion(Square square);
	
	Color getColor(Square square);
	
	Piece getPieza(Square square);

	long getPosiciones (Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWhitoutKing(Color color);

	PiecePlacementIterator iterator(SquareIterator squareIterator);
	
	PiecePlacementIterator iterator(long posiciones);
	
	PiecePlacementIterator iteratorAllPieces();	
	
	void constructBoardRepresentation(ChessPositionBuilder<?> builder);
}
