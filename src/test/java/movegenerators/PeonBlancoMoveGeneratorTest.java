package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import parsers.FENParser;

public class PeonBlancoMoveGeneratorTest {

	@Test
	public void testSaltoSimple() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/P7/8/8");
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.a3));
	
		PeonBlancoMoveGenerator moveGenerator = new PeonBlancoMoveGenerator();
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.a3);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains(new Move(Square.a3, Square.a4)));
	}
	
	@Test
	public void testSaltoDoble() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/P7/8");
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.a2));
	
		PeonBlancoMoveGenerator moveGenerator = new PeonBlancoMoveGenerator();
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.a2);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(Square.a2, Square.a3)));
		assertTrue(moves.contains(new Move(Square.a2, Square.a4)));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		org.junit.Assert.fail("Not implemented");
	}
	
	@Test
	public void testAtaqueDerecha() {
		org.junit.Assert.fail("Not implemented");
	}	

}
