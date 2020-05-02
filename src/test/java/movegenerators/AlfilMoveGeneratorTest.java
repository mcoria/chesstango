package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
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
	
	private Collection<Square> affectedBySquares;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new AlfilMoveGenerator(Color.BLANCO);
	}
	
	@Test
	public void testGetPseudoMoves01() {
		DummyBoard tablero = builder.withTablero("8/8/8/4B3/8/8/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);

		moveGenerator.generatePseudoMoves(origen);
		
		moves = moveGenerator.getMoveContainer();
		
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

		
		affectedBySquares = moveGenerator.getAffectedBy();
		
		// NorteEste
		assertTrue(affectedBySquares.contains( Square.f6 ));
		assertTrue(affectedBySquares.contains( Square.g7 ));
		assertTrue(affectedBySquares.contains( Square.h8 ));
		
		// SurEste
		assertTrue(affectedBySquares.contains( Square.f4 ));
		assertTrue(affectedBySquares.contains( Square.g3 ));
		assertTrue(affectedBySquares.contains( Square.h2 ));
		
		// SurOeste
		assertTrue(affectedBySquares.contains( Square.d4 ));
		assertTrue(affectedBySquares.contains( Square.c3 ));
		assertTrue(affectedBySquares.contains( Square.b2 ));
		assertTrue(affectedBySquares.contains( Square.a1 ));
		
		// NorteOeste
		assertTrue(affectedBySquares.contains( Square.d6 ));
		assertTrue(affectedBySquares.contains( Square.c7 ));
		assertTrue(affectedBySquares.contains( Square.b8 ));
		
		assertEquals(13, affectedBySquares.size());
		
	}
	


	@Test
	public void testGetPseudoMoves02() {
		DummyBoard tablero = builder.withTablero("8/8/8/6p1/8/8/PPP1PPPP/2B5").buildDummyBoard();
		moveGenerator.setTablero(tablero);

		Square from = Square.c1;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);

		moveGenerator.generatePseudoMoves(origen);
		
		moves = moveGenerator.getMoveContainer();

		//Moves
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.g5, Pieza.PEON_NEGRO) ));
		
		assertEquals(4, moves.size());
		
		

		affectedBySquares =  moveGenerator.getAffectedBy();

		//affectedBySquares
		assertTrue(affectedBySquares.contains( Square.d2 ));
		assertTrue(affectedBySquares.contains( Square.e3 ));
		assertTrue(affectedBySquares.contains( Square.f4 ));
		assertTrue(affectedBySquares.contains( Square.g5 ));
		assertTrue(affectedBySquares.contains( Square.b2 ));
		
		assertEquals(5, affectedBySquares.size());		

	}	

	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}	
}
