package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.DummyBoard;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;
public class CardinalMoveGeneratorSurTest {
	
	private FENBoarBuilder builder;
	
	private CardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testSur() {
		DummyBoard tablero = builder.withTablero("8/8/8/4R3/8/8/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.TORRE_BLANCO);	
	
		moveGenerator.setMoveColector(moves);
		moveGenerator.setSquaresColector(new ArrayList<Square>());
		moveGenerator.generateMoves(origen);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e1) ));
	}
	
	@Test
	public void testSur01() {
		DummyBoard tablero = builder.withTablero("8/8/8/4R3/8/8/8/4B3").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e1));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.TORRE_BLANCO);	
	
		moveGenerator.setMoveColector(moves);
		moveGenerator.setSquaresColector(new ArrayList<Square>());
		moveGenerator.generateMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
	}	
	
	@Test
	public void testSur02() {
		DummyBoard tablero = builder.withTablero("8/8/8/4R3/8/8/8/4b3").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.e1));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.TORRE_BLANCO);	
	
		moveGenerator.setMoveColector(moves);
		moveGenerator.setSquaresColector(new ArrayList<Square>());
		moveGenerator.generateMoves(origen);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains(  createCaptureMove(origen, Square.e1, Pieza.ALFIL_NEGRO) ));
	}	
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}	
		
}
