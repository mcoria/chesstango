package chess.iterators;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import chess.Square;
import chess.iterators.SaltoSquareIterator;
import chess.movesgenerators.ReyAbstractMoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class SaltoSquareIteratorTest01 {

	@Test
	public void test01() {
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.e5, ReyAbstractMoveGenerator.SALTOS_REY);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.a1, ReyAbstractMoveGenerator.SALTOS_REY);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.h1, ReyAbstractMoveGenerator.SALTOS_REY);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.a8, ReyAbstractMoveGenerator.SALTOS_REY);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.h8, ReyAbstractMoveGenerator.SALTOS_REY);
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
