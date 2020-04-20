package chess;

import iterators.BoardIterator;
import iterators.SquareIterator;

public interface DummyBoard extends Iterable<PosicionPieza> {

	///////////////////////////// START positioning logic /////////////////////////////
	// Quizas podria encapsular estas operaciones en su propia clase.
	// Bitboard podria ser mas rapido? Un word por tipo de ficha
	// Las primitivas de tablero son muy basicas!? En vez de descomponer una movimiento en operaciones simples, proporcionar un solo metodo
	//	
	PosicionPieza getPosicion(Square square);
	void setPosicion(PosicionPieza entry);

	Pieza getPieza(Square square);
	void setPieza(Square square, Pieza pieza);

	void setEmptySquare(Square square);
	boolean isEmtpy(Square square);
	
	// La operacion move supone que el destino esta VACIO
	void move(PosicionPieza from, Square to);
	
	// La operacion move supone que el destino esta OCUPADO
	void capture(PosicionPieza from, PosicionPieza to);
	

	BoardIterator iterator();

	BoardIterator iterator(SquareIterator squareIterator);
	///////////////////////////// END Board Iteration Logic /////////////////////////////	

}