package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class CaballoMoveGeneratorTest {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void test() {
		DummyBoard tablero = builder.withTablero("8/3P1p2/8/4N3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f7));		
	
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.CABALLO_BLANCO);
		
		CaballoMoveGenerator moveGenerator = new CaballoMoveGenerator(Color.BLANCO);
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
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
		return new SimpleMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza));
	}	
	
}
