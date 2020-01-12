package chess;

import static org.junit.Assert.*;

import org.junit.Test;

public class SquareTest {

	@Test
	public void testToString() {
		Square square = Square.a1;
		assertEquals("a1", square.toString());
	}

}
