package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import parsers.FENParser;

public class AlfilMoveGeneratorTest {

	@Test
	public void testGetPseudoMoves01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");

		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO);

		AlfilMoveGenerator moveGenerator = new AlfilMoveGenerator(Color.BLANCO);
		moveGenerator.setTablero(tablero);

		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);  

		
		// NorteEste
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h8) ));

		// SurEste
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h2) ));

		// SurOeste
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.a1) ));

		// NorteOeste
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b8) ));
		
		assertEquals(13, moves.size());		
	}
	


	@Test
	public void testGetPseudoMoves02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/6p1/8/8/PPP1PPPP/2B5");

		Square from = Square.c1;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g5));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO);

		AlfilMoveGenerator moveGenerator = new AlfilMoveGenerator(Color.BLANCO);
		moveGenerator.setTablero(tablero);

		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);  

		assertEquals(4, moves.size());
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.g5, Pieza.PEON_NEGRO) ));

	}	

	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}	
}
