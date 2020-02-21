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
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import iterators.CardinalSquareIterator.Cardinal;
import parsers.FENBoarBuilder;

public class CardinalMoveGeneratorNorteOesteTest {	
	
	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testNorteOeste() {
		DummyBoard tablero = builder.withTablero("8/8/8/4B3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO);
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.b8) ));
	}
	
	
	
	@Test
	public void testNorteOeste01() {
		DummyBoard tablero = builder.withTablero("1R6/8/8/4B3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.b8));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO);
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
	}
	
	
	@Test
	public void testNorteOeste02() {
		DummyBoard tablero = builder.withTablero("1r6/8/8/4B3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.b8));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO);
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c7) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.b8, Pieza.TORRE_NEGRO) ));
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}	
}
