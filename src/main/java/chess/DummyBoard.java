package chess;


import java.util.Iterator;

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

	void setEmptyPosicion(PosicionPieza captura);
	void setEmptySquare(Square square);
	boolean isEmtpy(Square square);
	
	// La operacion move es una primitiva
	void move(PosicionPieza from, PosicionPieza to);

	Iterator<PosicionPieza> iterator(SquareIterator squareIterator);
	
	Iterator<PosicionPieza> iterator(long posiciones);

}