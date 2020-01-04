package parsers;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.Pieza;

public class FENParserTest {

	@Test
	public void testParseRankBlakRank01() {
		FENParser parser = new FENParser();
		
		Pieza[] piezas = parser.parseRank("rnbqkbnr");
		
		assertEquals(Pieza.TORRE_NEGRO, piezas[0]);
		assertEquals(Pieza.CABALLO_NEGRO, piezas[1]);
		assertEquals(Pieza.ALFIL_NEGRO, piezas[2]);
		assertEquals(Pieza.REINA_NEGRO, piezas[3]);
		assertEquals(Pieza.REY_NEGRO, piezas[4]);
		assertEquals(Pieza.ALFIL_NEGRO, piezas[5]);
		assertEquals(Pieza.CABALLO_NEGRO, piezas[6]);
		assertEquals(Pieza.TORRE_NEGRO, piezas[7]);
	}
	
	@Test
	public void testParseRankBlakRank02() {
		FENParser parser = new FENParser();
		
		Pieza[] piezas = parser.parseRank("pppppppp");
		
		assertEquals(Pieza.PEON_NEGRO, piezas[0]);
		assertEquals(Pieza.PEON_NEGRO, piezas[1]);
		assertEquals(Pieza.PEON_NEGRO, piezas[2]);
		assertEquals(Pieza.PEON_NEGRO, piezas[3]);
		assertEquals(Pieza.PEON_NEGRO, piezas[4]);
		assertEquals(Pieza.PEON_NEGRO, piezas[5]);
		assertEquals(Pieza.PEON_NEGRO, piezas[6]);
		assertEquals(Pieza.PEON_NEGRO, piezas[7]);
	}
	
	@Test
	public void testParseRankWhiteRank01() {
		FENParser parser = new FENParser();
		
		Pieza[] piezas = parser.parseRank("RNBQKBNR");
		
		assertEquals(Pieza.TORRE_BLANCO, piezas[0]);
		assertEquals(Pieza.CABALLO_BLANCO, piezas[1]);
		assertEquals(Pieza.ALFIL_BLANCO, piezas[2]);
		assertEquals(Pieza.REINA_BLANCO, piezas[3]);
		assertEquals(Pieza.REY_BLANCO, piezas[4]);
		assertEquals(Pieza.ALFIL_BLANCO, piezas[5]);
		assertEquals(Pieza.CABALLO_BLANCO, piezas[6]);
		assertEquals(Pieza.TORRE_BLANCO, piezas[7]);
	}	
	
	@Test
	public void testParseRankWhiteRank02() {
		FENParser parser = new FENParser();
		
		Pieza[] piezas = parser.parseRank("PPPPPPPP");
		
		assertEquals(Pieza.PEON_BLANCO, piezas[0]);
		assertEquals(Pieza.PEON_BLANCO, piezas[1]);
		assertEquals(Pieza.PEON_BLANCO, piezas[2]);
		assertEquals(Pieza.PEON_BLANCO, piezas[3]);
		assertEquals(Pieza.PEON_BLANCO, piezas[4]);
		assertEquals(Pieza.PEON_BLANCO, piezas[5]);
		assertEquals(Pieza.PEON_BLANCO, piezas[6]);
		assertEquals(Pieza.PEON_BLANCO, piezas[7]);
	}		
	
	@Test
	public void testParsePiecePlacement() {
		FENParser parser = new FENParser();
		
		Pieza[] piezas = parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		assertEquals(Pieza.TORRE_BLANCO, piezas[0]);
		assertEquals(Pieza.CABALLO_BLANCO, piezas[1]);
		assertEquals(Pieza.ALFIL_BLANCO, piezas[2]);
		assertEquals(Pieza.REINA_BLANCO, piezas[3]);
		assertEquals(Pieza.REY_BLANCO, piezas[4]);
		assertEquals(Pieza.ALFIL_BLANCO, piezas[5]);
		assertEquals(Pieza.CABALLO_BLANCO, piezas[6]);
		assertEquals(Pieza.TORRE_BLANCO, piezas[7]);
		
		assertEquals(Pieza.PEON_BLANCO, piezas[8]);
		assertEquals(Pieza.PEON_BLANCO, piezas[9]);
		assertEquals(Pieza.PEON_BLANCO, piezas[10]);
		assertEquals(Pieza.PEON_BLANCO, piezas[11]);
		assertEquals(Pieza.PEON_BLANCO, piezas[12]);
		assertEquals(Pieza.PEON_BLANCO, piezas[13]);
		assertEquals(Pieza.PEON_BLANCO, piezas[14]);
		assertEquals(Pieza.PEON_BLANCO, piezas[15]);
		
		for (int i = 16; i < 48; i++) {
			assertNull(piezas[i]);
		}
		
		assertEquals(Pieza.PEON_NEGRO, piezas[48]);
		assertEquals(Pieza.PEON_NEGRO, piezas[49]);
		assertEquals(Pieza.PEON_NEGRO, piezas[50]);
		assertEquals(Pieza.PEON_NEGRO, piezas[51]);
		assertEquals(Pieza.PEON_NEGRO, piezas[52]);
		assertEquals(Pieza.PEON_NEGRO, piezas[53]);
		assertEquals(Pieza.PEON_NEGRO, piezas[54]);
		assertEquals(Pieza.PEON_NEGRO, piezas[55]);		
		
		assertEquals(Pieza.TORRE_NEGRO, piezas[56]);
		assertEquals(Pieza.CABALLO_NEGRO, piezas[57]);
		assertEquals(Pieza.ALFIL_NEGRO, piezas[58]);
		assertEquals(Pieza.REINA_NEGRO, piezas[59]);
		assertEquals(Pieza.REY_NEGRO, piezas[60]);
		assertEquals(Pieza.ALFIL_NEGRO, piezas[61]);
		assertEquals(Pieza.CABALLO_NEGRO, piezas[62]);
		assertEquals(Pieza.TORRE_NEGRO, piezas[63]);
	}		

}
