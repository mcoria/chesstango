package chess.board.iterators.bysquare;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chess.board.Square;
import chess.board.iterators.SquareIterator;


/**
 * @author Mauricio Coria
 *
 */
public class BitSquareIteratorTest {

	@Test
	public void test() {
		long posiciones = 0;
		posiciones |= Square.a1.getPosicion();
		posiciones |= Square.b6.getPosicion();
		
		List<Square> squares = new ArrayList<Square>();
		
		for (SquareIterator iterator = new PositionsSquareIterator(posiciones); iterator.hasNext();) {
			squares.add(iterator.next());
		}
		
		assertTrue(squares.contains(Square.a1));
		assertTrue(squares.contains(Square.b6));
		assertEquals(2, squares.size());
	}

}
