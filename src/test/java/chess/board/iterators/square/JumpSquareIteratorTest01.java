package chess.board.iterators.square;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import chess.board.Square;
import chess.board.iterators.square.JumpSquareIterator;
import chess.board.pseudomovesgenerators.strategies.AbstractKingMoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class JumpSquareIteratorTest01 {

	@Test
	public void test01() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.e5, AbstractKingMoveGenerator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		assertEquals(8, squares.size());
		assertTrue(squares.contains(Square.d6));
		assertTrue(squares.contains(Square.e6));
		assertTrue(squares.contains(Square.f6));
		assertTrue(squares.contains(Square.d4));
		assertTrue(squares.contains(Square.e4));
		assertTrue(squares.contains(Square.f4));
		assertTrue(squares.contains(Square.d5));
		assertTrue(squares.contains(Square.f5));
	}

	@Test
	public void test02() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a1, AbstractKingMoveGenerator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		assertEquals(3, squares.size());
		assertTrue(squares.contains(Square.a2));
		assertTrue(squares.contains(Square.b2));
		assertTrue(squares.contains(Square.b1));
	}
	
	@Test
	public void test03() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h1, AbstractKingMoveGenerator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		assertTrue(squares.contains(Square.h2));
		assertTrue(squares.contains(Square.g2));
		assertTrue(squares.contains(Square.g1));
		assertEquals(3, squares.size());
	}
	
	@Test
	public void test04() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a8, AbstractKingMoveGenerator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		assertEquals(3, squares.size());
		assertTrue(squares.contains(Square.a7));
		assertTrue(squares.contains(Square.b7));
		assertTrue(squares.contains(Square.b8));
	}
	
	@Test
	public void test05() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h8, AbstractKingMoveGenerator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		assertEquals(3, squares.size());
		assertTrue(squares.contains(Square.g8));
		assertTrue(squares.contains(Square.g7));
		assertTrue(squares.contains(Square.h7));
	}		
}
