package movegenerators;

import static org.junit.Assert.*;

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

public class CaballoMoveGeneratorTest {

	@Test
	public void test() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/3P1p2/8/4N3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f7));		
	
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.CABALLO_BLANCO);
		
		CaballoMoveGenerator moveGenerator = new CaballoMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.CABALLO_BLANCO));
		
		assertEquals(7, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.g6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c6) ));
		// Peon Blanco en d7
		assertTrue(moves.contains( createCaptureMove(origen, Square.f7, Pieza.PEON_NEGRO) ));
	}

	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}	
	
}
