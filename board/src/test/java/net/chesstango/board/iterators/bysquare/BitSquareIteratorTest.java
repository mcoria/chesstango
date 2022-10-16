package net.chesstango.board.iterators.bysquare;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;


/**
 * @author Mauricio Coria
 *
 */
public class BitSquareIteratorTest {

	@Test
	public void test() {
		long posiciones = 0;
		posiciones |= Square.a1.getBitPosition();
		posiciones |= Square.b6.getBitPosition();
		
		List<Square> squares = new ArrayList<Square>();
		
		for (SquareIterator iterator = new PositionsSquareIterator(posiciones); iterator.hasNext();) {
			squares.add(iterator.next());
		}
		
		assertTrue(squares.contains(Square.a1));
		assertTrue(squares.contains(Square.b6));
		assertEquals(2, squares.size());
	}

}
