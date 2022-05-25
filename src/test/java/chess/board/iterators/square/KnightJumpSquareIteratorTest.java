package chess.board.iterators.square;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import chess.board.iterators.square.bypiece.KnightJumpSquareIterator;
import org.junit.Test;

import chess.board.Square;


/**
 * @author Mauricio Coria
 *
 */
public class KnightJumpSquareIteratorTest {

	@Test
	public void test01() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.e5, KnightJumpSquareIterator.KNIGHT_JUMPS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
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
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a1, KnightJumpSquareIterator.KNIGHT_JUMPS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}


		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.b3));
		assertTrue(squares.contains(Square.c2));
	}

	@Test
	public void test03() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h1, KnightJumpSquareIterator.KNIGHT_JUMPS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.g3));
		assertTrue(squares.contains(Square.f2));
	}

	@Test
	public void test04() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a8, KnightJumpSquareIterator.KNIGHT_JUMPS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}


		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.b6));
		assertTrue(squares.contains(Square.c7));
	}

	@Test
	public void test05() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h8, KnightJumpSquareIterator.KNIGHT_JUMPS);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		assertEquals(2, squares.size());
		assertTrue(squares.contains(Square.f7));
		assertTrue(squares.contains(Square.g6));
	}

}
