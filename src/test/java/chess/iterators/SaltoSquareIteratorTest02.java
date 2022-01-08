package chess.iterators;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import chess.Square;
import chess.iterators.SaltoSquareIterator;
import chess.movesgenerators.CaballoMoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class SaltoSquareIteratorTest02 {

	@Test
	public void test01() {
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.e5, CaballoMoveGenerator.SALTOS_CABALLO);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.a1, CaballoMoveGenerator.SALTOS_CABALLO);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.h1, CaballoMoveGenerator.SALTOS_CABALLO);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.a8, CaballoMoveGenerator.SALTOS_CABALLO);
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
		SaltoSquareIterator iterator = new SaltoSquareIterator(Square.h8, CaballoMoveGenerator.SALTOS_CABALLO);
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
