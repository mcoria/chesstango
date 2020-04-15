package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;
public class PeonNegroMoveGeneratorTest {
	
	private FENBoarBuilder builder;
	
	private PeonNegroMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new PeonNegroMoveGenerator();
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testSaltoSimple() {
		Board tablero = builder.withTablero("8/8/p7/8/8/8/8/8").buildBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setBoardState(tablero.getBoardState());
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		Square from = Square.a6;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
	}
	
	@Test
	public void testSaltoDoble() {		
		Board tablero = builder.withTablero("8/p7/8/8/8/8/8/8").buildBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setBoardState(tablero.getBoardState());
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		Square from = Square.a7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a5, Square.a6) ));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		Board tablero = builder.withTablero("8/4p3/3P4/8/8/8/8/8").buildBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setBoardState(tablero.getBoardState());
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d6));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.a6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d6, Pieza.PEON_BLANCO) ));
	}
	
	@Test
	public void testPeonPasanteIzquierda() {
		Board tablero = 
				builder
				.withTablero("8/8/8/8/3Pp3/8/8/8")
				.withPeonPasanteSquare(Square.d3)
				.buildBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setBoardState(tablero.getBoardState());
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		Square from = Square.e4;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d4));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.d3) ));
	}
	
	@Test
	public void testAtaqueDerecha() {
		Board tablero = 
				builder
				.withTablero("8/4p3/5P2/8/8/8/8/8")
				.buildBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setBoardState(tablero.getBoardState());
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.f6));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.e6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f6, Pieza.PEON_BLANCO) ));
		
		assertEquals(3, moves.size());
	}
	
	@Test
	public void testPeonPasanteDerecha() {
		Board tablero = 
				builder
				.withTablero("8/8/8/8/3pP3/8/8/8")
				.withPeonPasanteSquare(Square.e3)
				.buildBoard();
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setBoardState(tablero.getBoardState());
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		Square from = Square.d4;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e4));

		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.e3) ));
	}
	
	@Test
	public void testPuedeCapturarRey() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/8/6p1/4K2R").buildBoard();
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(Square.e1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));

		PosicionPieza origen = new PosicionPieza(Square.g2, Pieza.PEON_NEGRO);

		assertTrue( moveGenerator.puedeCapturarRey(origen, Square.f1) );
	}	
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(PosicionPieza origen, Square destinoSquare, Square squarePasante) {
		return new SaltoDoblePeonMove(origen, new PosicionPieza(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}

	private Move createCapturePeonPasanteMove(PosicionPieza origen, Square destinoSquare) {
		return new CapturePeonPasante(origen, new PosicionPieza(destinoSquare, null), new PosicionPieza(Square.getSquare(destinoSquare.getFile(), 3), Pieza.PEON_BLANCO));
	}	

}
