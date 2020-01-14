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
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class TorreMoveGeneratorTest {

	@Test
	public void testGetPseudoMoves01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(14, moves.size());
		
		//Norte
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e8, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Sur
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e1, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Este
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Oeste
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));		
	}
	
	
	@Test
	public void testGetPseudoMoves02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/4p3/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e7));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO));
		
		assertEquals(13, moves.size());
		
		//Norte
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e7, new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.PEON_NEGRO))));
		
		//Sur
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e3, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e2, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e1, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		
		//Este
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		
		//Oeste
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));		
	}	

}
