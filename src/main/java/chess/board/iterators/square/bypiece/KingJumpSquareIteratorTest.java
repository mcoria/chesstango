package chess.board.iterators.square.bypiece;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import chess.board.iterators.square.JumpSquareIterator;
import chess.board.iterators.square.bypiece.KingJumpSquareIterator;
import org.junit.Test;

import chess.board.Square;


/**
 * @author Mauricio Coria
 *
 */
public class KingJumpSquareIteratorTest {

	@Test
	public void test01() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.e5, KingJumpSquareIterator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		Assert.assertEquals(8, squares.size());
		Assert.assertTrue(squares.contains(Square.d6));
		Assert.assertTrue(squares.contains(Square.e6));
		Assert.assertTrue(squares.contains(Square.f6));
		Assert.assertTrue(squares.contains(Square.d4));
		Assert.assertTrue(squares.contains(Square.e4));
		Assert.assertTrue(squares.contains(Square.f4));
		Assert.assertTrue(squares.contains(Square.d5));
		Assert.assertTrue(squares.contains(Square.f5));
	}

	@Test
	public void test02() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a1, KingJumpSquareIterator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		Assert.assertEquals(3, squares.size());
		Assert.assertTrue(squares.contains(Square.a2));
		Assert.assertTrue(squares.contains(Square.b2));
		Assert.assertTrue(squares.contains(Square.b1));
	}
	
	@Test
	public void test03() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h1, KingJumpSquareIterator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		Assert.assertTrue(squares.contains(Square.h2));
		Assert.assertTrue(squares.contains(Square.g2));
		Assert.assertTrue(squares.contains(Square.g1));
		Assert.assertEquals(3, squares.size());
	}
	
	@Test
	public void test04() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.a8, KingJumpSquareIterator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		
		Assert.assertEquals(3, squares.size());
		Assert.assertTrue(squares.contains(Square.a7));
		Assert.assertTrue(squares.contains(Square.b7));
		Assert.assertTrue(squares.contains(Square.b8));
	}
	
	@Test
	public void test05() {
		JumpSquareIterator iterator = new JumpSquareIterator(Square.h8, KingJumpSquareIterator.SALTOS_KING);
		Set<Square> squares = new HashSet<Square>();

		while (iterator.hasNext()) {
			Square square = iterator.next();
			//System.out.println(square);
			squares.add(square);
		}

		Assert.assertEquals(3, squares.size());
		Assert.assertTrue(squares.contains(Square.g8));
		Assert.assertTrue(squares.contains(Square.g7));
		Assert.assertTrue(squares.contains(Square.h7));
	}

}
