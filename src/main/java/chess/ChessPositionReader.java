/**
 * 
 */
package chess;

import chess.builder.ChessPositionBuilder;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.iterators.square.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader {

	Color getTurnoActual();

	public boolean isCastlingWhiteQueenAllowed();
	public boolean isCastlingWhiteKingAllowed();

	public boolean isCastlingBlackQueenAllowed();
	public boolean isCastlingBlackKingAllowed();

	Square getPawnPasanteSquare();
	
	PiecePositioned getPosicion(Square square);
	
	Color getColor(Square square);
	
	Piece getPieza(Square square);

	Square getKingSquare(Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWhitoutKing(Color color);

	PiecePlacementIterator iterator(SquareIterator squareIterator);
	
	void constructBoardRepresentation(ChessPositionBuilder<?> builder);	
}
