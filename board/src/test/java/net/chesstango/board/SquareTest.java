package net.chesstango.board;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Mauricio Coria
 *
 */
public class SquareTest {

	@Test
	public void testToString() {
		Square square = Square.a1;
		assertEquals("a1", square.toString());
	}
	
	@Test
	public void testGetSquare() {
		assertEquals(Square.a1, Square.getSquare(0, 0));
		assertEquals(Square.b1, Square.getSquare(1, 0));
		assertEquals(Square.c1, Square.getSquare(2, 0));
		assertEquals(Square.d1, Square.getSquare(3, 0));
		assertEquals(Square.e1, Square.getSquare(4, 0));
		assertEquals(Square.f1, Square.getSquare(5, 0));
		assertEquals(Square.g1, Square.getSquare(6, 0));
		assertEquals(Square.h1, Square.getSquare(7, 0));

		assertEquals(Square.a2, Square.getSquare(0, 1));
		assertEquals(Square.b2, Square.getSquare(1, 1));
		assertEquals(Square.c2, Square.getSquare(2, 1));
		assertEquals(Square.d2, Square.getSquare(3, 1));
		assertEquals(Square.e2, Square.getSquare(4, 1));
		assertEquals(Square.f2, Square.getSquare(5, 1));
		assertEquals(Square.g2, Square.getSquare(6, 1));
		assertEquals(Square.h2, Square.getSquare(7, 1));

		assertEquals(Square.a3, Square.getSquare(0, 2));
		assertEquals(Square.b3, Square.getSquare(1, 2));
		assertEquals(Square.c3, Square.getSquare(2, 2));
		assertEquals(Square.d3, Square.getSquare(3, 2));
		assertEquals(Square.e3, Square.getSquare(4, 2));
		assertEquals(Square.f3, Square.getSquare(5, 2));
		assertEquals(Square.g3, Square.getSquare(6, 2));
		assertEquals(Square.h3, Square.getSquare(7, 2));

		assertEquals(Square.a4, Square.getSquare(0, 3));
		assertEquals(Square.b4, Square.getSquare(1, 3));
		assertEquals(Square.c4, Square.getSquare(2, 3));
		assertEquals(Square.d4, Square.getSquare(3, 3));
		assertEquals(Square.e4, Square.getSquare(4, 3));
		assertEquals(Square.f4, Square.getSquare(5, 3));
		assertEquals(Square.g4, Square.getSquare(6, 3));
		assertEquals(Square.h4, Square.getSquare(7, 3));

		assertEquals(Square.a5, Square.getSquare(0, 4));
		assertEquals(Square.b5, Square.getSquare(1, 4));
		assertEquals(Square.c5, Square.getSquare(2, 4));
		assertEquals(Square.d5, Square.getSquare(3, 4));
		assertEquals(Square.e5, Square.getSquare(4, 4));
		assertEquals(Square.f5, Square.getSquare(5, 4));
		assertEquals(Square.g5, Square.getSquare(6, 4));
		assertEquals(Square.h5, Square.getSquare(7, 4));

		assertEquals(Square.a6, Square.getSquare(0, 5));
		assertEquals(Square.b6, Square.getSquare(1, 5));
		assertEquals(Square.c6, Square.getSquare(2, 5));
		assertEquals(Square.d6, Square.getSquare(3, 5));
		assertEquals(Square.e6, Square.getSquare(4, 5));
		assertEquals(Square.f6, Square.getSquare(5, 5));
		assertEquals(Square.g6, Square.getSquare(6, 5));
		assertEquals(Square.h6, Square.getSquare(7, 5));
		
		assertEquals(Square.a7, Square.getSquare(0, 6));
		assertEquals(Square.b7, Square.getSquare(1, 6));
		assertEquals(Square.c7, Square.getSquare(2, 6));
		assertEquals(Square.d7, Square.getSquare(3, 6));
		assertEquals(Square.e7, Square.getSquare(4, 6));
		assertEquals(Square.f7, Square.getSquare(5, 6));
		assertEquals(Square.g7, Square.getSquare(6, 6));
		assertEquals(Square.h7, Square.getSquare(7, 6));

		assertEquals(Square.a8, Square.getSquare(0, 7));
		assertEquals(Square.b8, Square.getSquare(1, 7));
		assertEquals(Square.c8, Square.getSquare(2, 7));
		assertEquals(Square.d8, Square.getSquare(3, 7));
		assertEquals(Square.e8, Square.getSquare(4, 7));
		assertEquals(Square.f8, Square.getSquare(5, 7));
		assertEquals(Square.g8, Square.getSquare(6, 7));
		assertEquals(Square.h8, Square.getSquare(7, 7));
	}
	
	@Test
	public void testPosicion() {
		assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000001L, Square.a1.getBitPosition());
		assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000010L, Square.b1.getBitPosition());
		assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_00000000L, Square.a2.getBitPosition());
		assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000010_00000000L, Square.b2.getBitPosition());
		
		assertEquals(0b01000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, Square.g8.getBitPosition());
		assertEquals(0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, Square.h8.getBitPosition());
	}
	
	@Test
	public void testPosicion01() {
		long posiciones = 0;
		Square[] values = Square.values();
		for (int i = 0; i < values.length; i++) {
			posiciones &= values[i].getBitPosition();
		}
		
		assertEquals(0, posiciones);
	}

	@Test
	public void testMirror() {
		assertEquals(Square.a8, Square.a1.getMirrorSquare());
		assertEquals(Square.h1, Square.h8.getMirrorSquare());
	}

	@Test
	public void testIdx() {
		for (Square square: Square.values()) {
			Assert.assertEquals(square, Square.getSquareByIdx(square.toIdx()));
		}
	}
}
