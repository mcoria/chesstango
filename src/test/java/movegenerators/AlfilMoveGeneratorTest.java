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
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class AlfilMoveGeneratorTest {

	private FENBoarBuilder builder;
	
	private AlfilMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new AlfilMoveGenerator(Color.BLANCO);
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testGetPseudoMoves01() {
		DummyBoard tablero = builder.withTablero("8/8/8/4B3/8/8/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);

		moveGenerator.generateMoves(origen, moves);
		
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
		DummyBoard tablero = builder.withTablero("8/8/8/6p1/8/8/PPP1PPPP/2B5").buildDummyBoard();
		moveGenerator.setTablero(tablero);

		Square from = Square.c1;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);

		moveGenerator.generateMoves(origen, moves);  

		assertEquals(4, moves.size());
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.g5, Pieza.PEON_NEGRO) ));

	}	

	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}	
}
