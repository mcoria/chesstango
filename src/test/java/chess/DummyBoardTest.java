package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class DummyBoardTest {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void test01() {
		DummyBoard tablero = builder.withDefaultBoard().buildDummyBoard();
		
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Collection<Move> moves = tablero.getLegalMoves();
		
		assertEquals(20, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(Square.a2, Pieza.PEON_BLANCO, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.a2, Pieza.PEON_BLANCO, Square.a4) ));
		assertTrue(moves.contains( createSimpleMove(Square.b2, Pieza.PEON_BLANCO, Square.b3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.b2, Pieza.PEON_BLANCO, Square.b4) ));
		assertTrue(moves.contains( createSimpleMove(Square.c2, Pieza.PEON_BLANCO, Square.c3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.c2, Pieza.PEON_BLANCO, Square.c4) ));
		assertTrue(moves.contains( createSimpleMove(Square.d2, Pieza.PEON_BLANCO, Square.d3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.d2, Pieza.PEON_BLANCO, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(Square.e2, Pieza.PEON_BLANCO, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.e2, Pieza.PEON_BLANCO, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(Square.f2, Pieza.PEON_BLANCO, Square.f3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.f2, Pieza.PEON_BLANCO, Square.f4) ));
		assertTrue(moves.contains( createSimpleMove(Square.g2, Pieza.PEON_BLANCO, Square.g3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.g2, Pieza.PEON_BLANCO, Square.g4) ));
		assertTrue(moves.contains( createSimpleMove(Square.h2, Pieza.PEON_BLANCO, Square.h3) ));
		assertTrue(moves.contains( createSaltoDobleMove(Square.h2, Pieza.PEON_BLANCO, Square.h4) ));
		
		//Caballo Reyna
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.a3) ));
		assertTrue(moves.contains( createSimpleMove(Square.b1, Pieza.CABALLO_BLANCO, Square.c3) ));
		
		//Caballo Rey
		assertTrue(moves.contains( createSimpleMove(Square.g1, Pieza.CABALLO_BLANCO, Square.f3) ));
		assertTrue(moves.contains( createSimpleMove(Square.g1, Pieza.CABALLO_BLANCO, Square.h3) ));
		
		//State
		assertEquals(Color.BLANCO, tablero.getBoardState().getTurnoActual());
		assertNull(tablero.getBoardState().getPeonPasanteSquare());
	}
	
	private Move createSimpleMove(Square origenSquare, Pieza origenPieza, Square destinoSquare) {
		return new SimpleMove(new SimpleImmutableEntry<Square, Pieza>(origenSquare, origenPieza), new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(Square origen, Pieza pieza, Square destinoSquare) {
		return new SaltoDoblePeonMove(new SimpleImmutableEntry<Square, Pieza>(origen, pieza), new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}		
		
}
