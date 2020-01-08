package iterators;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import chess.Square;

public class ReySquareIteratorTest {

	@Test
	public void test01() {
		ReySquareIterator iterator = new ReySquareIterator(Square.e5);
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
		ReySquareIterator iterator = new ReySquareIterator(Square.a1);
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
		ReySquareIterator iterator = new ReySquareIterator(Square.h1);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		assertEquals(3, squares.size());
		assertTrue(squares.contains(Square.h2));
		assertTrue(squares.contains(Square.g2));
		assertTrue(squares.contains(Square.g1));
	}
	
	@Test
	public void test04() {
		ReySquareIterator iterator = new ReySquareIterator(Square.a8);
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
		ReySquareIterator iterator = new ReySquareIterator(Square.h8);
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
