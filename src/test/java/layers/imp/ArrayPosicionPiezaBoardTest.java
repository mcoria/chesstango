package layers.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.posicionpieza.BoardBitIterator;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;
import parsers.FENBoarBuilder;

public class ArrayPosicionPiezaBoardTest {


	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void test() {
		PosicionPiezaBoard tablero = builder.withDefaultBoard().buildPosicionPiezaBoard();
		ArrayPosicionPiezaBoard tableroImp = (ArrayPosicionPiezaBoard) tablero;
		
		long posiciones = 0;
		posiciones |= Square.a1.getPosicion();
		posiciones |= Square.b7.getPosicion();
		posiciones |= Square.b8.getPosicion();
		posiciones |= Square.e1.getPosicion();
		posiciones |= Square.e8.getPosicion();
		
		List<PosicionPieza> posicionesList = new ArrayList<PosicionPieza>();

		for (Iterator<PosicionPieza> iterator =  new BoardBitIterator(tableroImp.tablero, posiciones); iterator.hasNext();) {
			posicionesList.add(iterator.next());
		}
		

		assertTrue(posicionesList.contains(new PosicionPieza(Square.a1, Pieza.TORRE_BLANCO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.b7, Pieza.PEON_NEGRO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.b8, Pieza.CABALLO_NEGRO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.e1, Pieza.REY_BLANCO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.e8, Pieza.REY_NEGRO)));
		assertEquals(5, posicionesList.size());

	}

}
