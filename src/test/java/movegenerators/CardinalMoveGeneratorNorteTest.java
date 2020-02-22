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
import iterators.CardinalSquareIterator.Cardinal;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class CardinalMoveGeneratorNorteTest {
	
	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testNorte() {
		DummyBoard tablero = builder.withTablero("8/8/8/4R3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO);		
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e8) ));
	}
	
	@Test
	public void testNorte01() {
		DummyBoard tablero = builder.withTablero("4B3/8/8/4R3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e8));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO);	
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
	}	
	
	@Test
	public void testNorte02() {
		DummyBoard tablero = builder.withTablero("4b3/8/8/4R3/8/8/8/8").buildDummyBoard();
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.e8));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO);
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		moveGenerator.setTablero(tablero);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e8, Pieza.ALFIL_NEGRO) ));
	}	
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new SimpleMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza));
	}
		
}
