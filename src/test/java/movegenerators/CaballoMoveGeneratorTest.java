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

public class CaballoMoveGeneratorTest {

	private FENBoarBuilder builder;
	
	private CaballoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new CaballoMoveGenerator(Color.BLANCO);
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void test() {
		DummyBoard tablero = builder.withTablero("8/3P1p2/8/4N3/8/8/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f7));		
	
		PosicionPieza origen = new PosicionPieza(from, Pieza.CABALLO_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.g6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.c6) ));
		// Peon Blanco en d7
		assertTrue(moves.contains( createCaptureMove(origen, Square.f7, Pieza.PEON_NEGRO) ));
		
		assertEquals(7, moves.size());
	}

	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}	
	
}
