package net.chesstango.board.iterators.bysquare;

import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PositionsSquareIterator implements SquareIterator {

	private final static Square[] array = {  
			Square.a1, Square.b1, Square.c1, Square.d1, Square.e1, Square.f1, Square.g1, Square.h1,
			Square.a2, Square.b2, Square.c2, Square.d2, Square.e2, Square.f2, Square.g2, Square.h2,
			Square.a3, Square.b3, Square.c3, Square.d3, Square.e3, Square.f3, Square.g3, Square.h3,
			Square.a4, Square.b4, Square.c4, Square.d4, Square.e4, Square.f4, Square.g4, Square.h4,
			Square.a5, Square.b5, Square.c5, Square.d5, Square.e5, Square.f5, Square.g5, Square.h5,
			Square.a6, Square.b6, Square.c6, Square.d6, Square.e6, Square.f6, Square.g6, Square.h6,
			Square.a7, Square.b7, Square.c7, Square.d7, Square.e7, Square.f7, Square.g7, Square.h7,
			Square.a8, Square.b8, Square.c8, Square.d8, Square.e8, Square.f8, Square.g8, Square.h8};
	
	private long positions;
	
	public PositionsSquareIterator(long positions) {
		this.positions = positions;
	}

	@Override
	public boolean hasNext() {
		return positions != 0;
	}

	@Override
	public Square next() {
		Square result = null;
		if (positions != 0) {
			long posicionLng = Long.lowestOneBit(positions);
			result  = array[Long.numberOfTrailingZeros(posicionLng)];
			positions &= ~posicionLng;
		}
		return result;
	}

}
