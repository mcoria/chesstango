package chess.board.iterators.square;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import chess.board.Square;
import chess.board.iterators.square.JumpSquareIterator;
import chess.board.pseudomovesgenerators.strategies.AbstractKingMoveGenerator;
import chess.board.pseudomovesgenerators.strategies.KnightMoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class JumpSquareIterator02Test {

	@Test
	public void test01() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.e5, KnightMoveGenerator.SALTOS_CABALLO);
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
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a1, KnightMoveGenerator.SALTOS_CABALLO);
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
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h1, KnightMoveGenerator.SALTOS_CABALLO);
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
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a8, KnightMoveGenerator.SALTOS_CABALLO);
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
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h8, KnightMoveGenerator.SALTOS_CABALLO);
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
	
	
	@Test
	public void test06() {
		long arraySaltos[] = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquare(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, KnightMoveGenerator.SALTOS_CABALLO);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				
				posicionesSalto |= salto.getPosicion();
			}			
			arraySaltos[idx] = posicionesSalto;
		}
		
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}

	}
	
	@Test
	public void test07() {
		long arraySaltos[] = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquare(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, AbstractKingMoveGenerator.SALTOS_KING);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				
				posicionesSalto |= salto.getPosicion();
			}			
			arraySaltos[idx] = posicionesSalto;
		}
		
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}

	}
	
	
	private final int[][] casillerosPawnWhite = {
			{ -1, -1 }, 
			{ 1, -1 }
		};
	
	@Test
	public void test08() {
		long arraySaltos[] = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquare(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, casillerosPawnWhite);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				
				posicionesSalto |= salto.getPosicion();
			}			
			arraySaltos[idx] = posicionesSalto;
		}
		
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}

	}
	
	private final int[][] casillerosPawnBlack = {
			{ -1, 1 }, 
			{ 1, 1 }
		};	
	
	@Test
	public void test09() {
		long arraySaltos[] = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquare(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, casillerosPawnBlack);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				
				posicionesSalto |= salto.getPosicion();
			}			
			arraySaltos[idx] = posicionesSalto;
		}
		
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}

	}	
	
}
