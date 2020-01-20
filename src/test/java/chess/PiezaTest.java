package chess;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiezaTest {

	@Test
	public void testBlanco() {
		assertEquals(Color.BLANCO, Pieza.PEON_BLANCO.getColor());
		assertEquals(Color.BLANCO, Pieza.TORRE_BLANCO.getColor());
		assertEquals(Color.BLANCO, Pieza.CABALLO_BLANCO.getColor());
		assertEquals(Color.BLANCO, Pieza.ALFIL_BLANCO.getColor());
		assertEquals(Color.BLANCO, Pieza.REINA_BLANCO.getColor());
		assertEquals(Color.BLANCO, Pieza.REY_BLANCO.getColor());
	}
	
	@Test
	public void testNegro() {
		assertEquals(Color.NEGRO, Pieza.PEON_NEGRO.getColor());
		assertEquals(Color.NEGRO, Pieza.TORRE_NEGRO.getColor());
		assertEquals(Color.NEGRO, Pieza.CABALLO_NEGRO.getColor());
		assertEquals(Color.NEGRO, Pieza.ALFIL_NEGRO.getColor());
		assertEquals(Color.NEGRO, Pieza.REINA_NEGRO.getColor());
		assertEquals(Color.NEGRO, Pieza.REY_NEGRO.getColor());
	}	

}
