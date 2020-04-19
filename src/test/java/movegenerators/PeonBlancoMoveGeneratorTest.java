package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CapturaPeonPromocion;
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import moveexecutors.SimplePeonPromocion;
import parsers.FENBoarBuilder;
public class PeonBlancoMoveGeneratorTest {
	
	private FENBoarBuilder builder;
	
	private PeonBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moves = new ArrayList<Move>();
		state = new BoardState();
		
		moveGenerator = new PeonBlancoMoveGenerator();
		moveGenerator.setBoardState(state);
	}
	
	@Test
	public void testSaltoSimple() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/P7/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a3;		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/8/P7/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4, Square.a3) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/N7/P7/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/3p4/4P3/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d3));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonPasanteIzquierda() {
		DummyBoard tablero =  builder.withTablero("8/8/8/3pP3/8/8/8/8").buildDummyBoard();
		
		state.setPeonPasanteSquare(Square.d6);
		
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.d6) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/5p2/4P3/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f3));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonPasanteDerecha() {
		DummyBoard tablero = builder.withTablero("8/8/8/3Pp3/8/8/8/8").buildDummyBoard();
		
		state.setPeonPasanteSquare(Square.e6);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.e6) ));
	}
	
	@Test
	public void testPeonSimplePeonPromocion() {
		DummyBoard tablero = builder.withTablero("8/3P4/8/8/8/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.TORRE_BLANCO) ));
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.CABALLO_BLANCO) ));
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.ALFIL_BLANCO) ));
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.REINA_BLANCO) ));
		
		assertEquals(4, moves.size());
	}

	@Test
	public void testPeonCapturaPeonPromocion() {
		DummyBoard tablero = builder.withTablero("2rr4/3P4/8/8/8/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.c8));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.d8));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.TORRE_BLANCO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.CABALLO_BLANCO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.ALFIL_BLANCO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.REINA_BLANCO) ));
		
		assertEquals(4, moves.size());
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
		return new CapturePeonPasante(origen, new PosicionPieza(destinoSquare, null), new PosicionPieza(Square.getSquare(destinoSquare.getFile(), 4), Pieza.PEON_NEGRO));
	}
	
	private Move createSimplePeonPromocion(PosicionPieza origen, Square destinoSquare, Pieza promocion) {
		return new SimplePeonPromocion(origen, new PosicionPieza(destinoSquare, null), promocion);
	}	
	
	private Move createCapturePeonPromocion(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza, Pieza promocion) {
		return new CapturaPeonPromocion(origen, new PosicionPieza(destinoSquare, destinoPieza), promocion);
	}	
	
}
