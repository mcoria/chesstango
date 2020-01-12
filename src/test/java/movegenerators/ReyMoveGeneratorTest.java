package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class ReyMoveGeneratorTest {

	@Test
	public void testNorte() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4K3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
	
		ReyMoveGenerator moveGenerator = new ReyMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO));
		
		assertEquals(8, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.REY_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.REY_BLANCO))));			
	}

}
