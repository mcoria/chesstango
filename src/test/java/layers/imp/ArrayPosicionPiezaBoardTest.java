package layers.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.posicionpieza.BoardBitIterator;

public class ArrayPosicionPiezaBoardTest {


	
	@Test
	public void test() {
		ArrayPosicionPiezaBoard tablero = new ArrayPosicionPiezaBoard();
		
		tablero.setPieza(Square.a1, Pieza.TORRE_BLANCO);
		tablero.setPieza(Square.b7, Pieza.PEON_NEGRO);
		tablero.setPieza(Square.b8, Pieza.CABALLO_NEGRO);
		tablero.setPieza(Square.e1, Pieza.REY_BLANCO);
		tablero.setPieza(Square.e8, Pieza.REY_NEGRO);
		
		
		// Al position should be not NULL (including emtpy squares)
		assertNotNull(tablero.getPosicion(Square.a1));
		assertNotNull(tablero.getPosicion(Square.b7));
		assertNotNull(tablero.getPosicion(Square.b8));
		assertNotNull(tablero.getPosicion(Square.e1));
		assertNotNull(tablero.getPosicion(Square.e8));
		assertNotNull(tablero.getPosicion(Square.e3));
		
		
		
		long posiciones = 0;
		posiciones |= Square.a1.getPosicion();
		posiciones |= Square.b7.getPosicion();
		posiciones |= Square.b8.getPosicion();
		posiciones |= Square.e1.getPosicion();
		posiciones |= Square.e8.getPosicion();
		posiciones |= Square.e3.getPosicion();
		
		List<PosicionPieza> posicionesList = new ArrayList<PosicionPieza>();

		for (Iterator<PosicionPieza> iterator =  new BoardBitIterator(tablero.tablero, posiciones); iterator.hasNext();) {
			posicionesList.add(iterator.next());
		}
		

		assertTrue(posicionesList.contains(new PosicionPieza(Square.a1, Pieza.TORRE_BLANCO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.b7, Pieza.PEON_NEGRO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.b8, Pieza.CABALLO_NEGRO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.e1, Pieza.REY_BLANCO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.e8, Pieza.REY_NEGRO)));
		assertTrue(posicionesList.contains(new PosicionPieza(Square.e3, null)));
		assertEquals(6, posicionesList.size());

	}

}
