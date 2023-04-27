package net.chesstango.board.iterators.bysquare;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
