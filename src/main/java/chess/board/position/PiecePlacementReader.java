/**
 * 
 */
package chess.board.position;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface PiecePlacementReader extends Iterable<PiecePositioned> {

	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	//	
	PiecePositioned getPosicion(Square square);

	Piece getPieza(Square square);

	boolean isEmtpy(Square square);

	PiecePlacementIterator iterator(SquareIterator squareIterator);

	PiecePlacementIterator iterator(long posiciones);

}