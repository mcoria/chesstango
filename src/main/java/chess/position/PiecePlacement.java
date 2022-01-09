package chess.position;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.pieceplacement.PiecePlacementIterator;
import chess.iterators.square.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface PiecePlacement extends Iterable<PiecePositioned> {

	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	//	
	PiecePositioned getPosicion(Square square);
	void setPosicion(PiecePositioned entry);

	Piece getPieza(Square square);
	void setPieza(Square square, Piece piece);

	void setEmptyPosicion(PiecePositioned captura);
	void setEmptySquare(Square square);
	boolean isEmtpy(Square square);
	
	// La operacion move es una primitiva
	void move(PiecePositioned from, PiecePositioned to);

	PiecePlacementIterator iterator(SquareIterator squareIterator);
	
	PiecePlacementIterator iterator(long posiciones);

}