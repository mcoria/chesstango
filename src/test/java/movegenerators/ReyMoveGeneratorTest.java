package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import chess.Move.MoveType;
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class ReyMoveGeneratorTest {

	@Test
	public void test01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4K3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
	
		ReyMoveGenerator moveGenerator = new ReyMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO));
		
		assertEquals(8, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}

	@Test
	public void test02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/4P3/4K3/4p3/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e6));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
	
		ReyMoveGenerator moveGenerator = new ReyMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO));
		
		assertEquals(7, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Pieza.PEON_NEGRO) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}
	
}
