package chess;

import iterators.BoardIterator;
import iterators.SquareIterator;

public interface DummyBoard extends Iterable<PosicionPieza> {

	PosicionPieza getPosicion(Square square);

	void setPosicion(PosicionPieza entry);

	Pieza getPieza(Square square);

	void setPieza(Square square, Pieza pieza);

	void setEmptySquare(Square square);

	boolean isEmtpy(Square square);
	///////////////////////////// END positioning logic /////////////////////////////

	///////////////////////////// START Board Iteration Logic /////////////////////////////
	BoardIterator iterator();

	BoardIterator iterator(SquareIterator squareIterator);
	///////////////////////////// END Board Iteration Logic /////////////////////////////	

}