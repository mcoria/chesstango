package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.AbstractMove;
import moveexecutors.Move;
import moveexecutors.SimpleMove;

public class AbstractMoveTest {

	@Before
	public void setUp() throws Exception {
		//builder = new FENBoarBuilder();
	}
	
	@Test
	public void testEquals01() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);
		
		assertEquals(new SimpleMove(origen, destino), new SimpleMove(origen, destino));
	}
	
	@Test
	public void testToString01() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);
		Move move = new SimpleMove(origen, destino);
		assertEquals("e5=TORRE_BLANCO e7=null - SimpleMove", move.toString());
	}	
	
	
	@Test
	public void testCompare01() {
		PosicionPieza a2 = new PosicionPieza(Square.a2, null);
		PosicionPieza a3 = new PosicionPieza(Square.a3, null);
		PosicionPieza a4 = new PosicionPieza(Square.a4, null);
		PosicionPieza b1 = new PosicionPieza(Square.b1, null);
		PosicionPieza b2 = new PosicionPieza(Square.b2, null);
		PosicionPieza b3 = new PosicionPieza(Square.b3, null);
		
		
		AbstractMove move1 = new SimpleMove(a2, a3);
		AbstractMove move2 = new SimpleMove(a2, a4);
		AbstractMove move3 = new SimpleMove(b2, b3);
		AbstractMove move4 = new SimpleMove(b1, a3);
		
		assertTrue(move1.compareTo(move2) > 0);
		assertTrue(move1.compareTo(move3) > 0);
		assertTrue(move1.compareTo(move4) > 0);
		
		assertTrue(move2.compareTo(move3) > 0);
		
		assertTrue(move3.compareTo(move4) > 0);

	}

}
