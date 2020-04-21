package iterators;

import chess.Square;

public class BitSquareIterator implements SquareIterator {

	private static Square[] array = {  
			Square.a1, Square.b1, Square.c1, Square.d1, Square.e1, Square.f1, Square.g1, Square.h1,
			Square.a2, Square.b2, Square.c2, Square.d2, Square.e2, Square.f2, Square.g2, Square.h2,
			Square.a3, Square.b3, Square.c3, Square.d3, Square.e3, Square.f3, Square.g3, Square.h3,
			Square.a4, Square.b4, Square.c4, Square.d4, Square.e4, Square.f4, Square.g4, Square.h4,
			Square.a5, Square.b5, Square.c5, Square.d5, Square.e5, Square.f5, Square.g5, Square.h5,
			Square.a6, Square.b6, Square.c6, Square.d6, Square.e6, Square.f6, Square.g6, Square.h6,
			Square.a7, Square.b7, Square.c7, Square.d7, Square.e7, Square.f7, Square.g7, Square.h7,
			Square.a8, Square.b8, Square.c8, Square.d8, Square.e8, Square.f8, Square.g8, Square.h8};
	
	private final long posiciones;
	
	private Square nextPoint;
	private int idx = 0;
	
	public BitSquareIterator(long posiciones) {
		this.posiciones = posiciones;
		calcularNextPoint();
	}
	
	@Override
	public boolean hasNext() {
		return this.nextPoint != null;
	}

	@Override
	public Square next() {
		Square currentPoint = this.nextPoint;
		calcularNextPoint();
		return currentPoint;
	}
	
	private void calcularNextPoint() {
		this.nextPoint = null;
		while (this.idx < 64 && nextPoint == null) {
			Square currentSquare = array[this.idx]; 
			this.nextPoint = (currentSquare.getPosicion() & this.posiciones) != 0 ? currentSquare : null;
			this.idx++;
		}
	}

}
