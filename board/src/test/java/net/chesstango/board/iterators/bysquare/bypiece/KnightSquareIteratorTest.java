package net.chesstango.board.iterators.bysquare.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.JumpSquareIterator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 *
 */
public class KnightSquareIteratorTest {

	@Test
	public void test01() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.e5, KnightSquareIterator.KNIGHT_JUMPS_OFFSETS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(bysquare);
			squares.add(square);
		}


		assertEquals(8, squares.size());
		assertTrue(squares.contains(Square.d7));
		assertTrue(squares.contains(Square.f7));
		assertTrue(squares.contains(Square.c4));
		assertTrue(squares.contains(Square.c6));
		assertTrue(squares.contains(Square.g4));
		assertTrue(squares.contains(Square.g6));
		assertTrue(squares.contains(Square.d3));
		assertTrue(squares.contains(Square.f3));
	}

	@Test
	public void test02() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a1, KnightSquareIterator.KNIGHT_JUMPS_OFFSETS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(bysquare);
			squares.add(square);
		}


		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.b3));
		assertTrue(squares.contains(Square.c2));
	}

	@Test
	public void test03() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h1, KnightSquareIterator.KNIGHT_JUMPS_OFFSETS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(bysquare);
			squares.add(square);
		}

		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.g3));
		assertTrue(squares.contains(Square.f2));
	}

	@Test
	public void test04() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a8, KnightSquareIterator.KNIGHT_JUMPS_OFFSETS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(bysquare);
			squares.add(square);
		}


		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.b6));
		assertTrue(squares.contains(Square.c7));
	}

	@Test
	public void test05() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h8, KnightSquareIterator.KNIGHT_JUMPS_OFFSETS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(bysquare);
			squares.add(square);
		}

		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.f7));
		assertTrue(squares.contains(Square.g6));
	}

}
