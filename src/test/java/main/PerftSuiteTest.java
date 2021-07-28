package main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PerftSuiteTest {
	
	private PerftSuite suite;
	
	@Before
	public void setUp() {
		suite = new PerftSuite();
		
	}
	
	@Test
	public void test_parse() {
		suite.parseTests("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ;D1 20 ;D2 400 ;D3 8902 ;D4 197281 ;D5 4865609 ;D6 119060324");
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", suite.fen);
		
		assertEquals(20, suite.perftResults[0]);
		assertEquals(400, suite.perftResults[1]);
		assertEquals(8902, suite.perftResults[2]);
		assertEquals(197281, suite.perftResults[3]);
		assertEquals(4865609, suite.perftResults[4]);
		assertEquals(119060324, suite.perftResults[5]);
		
	}
	
	
	@Test
	public void test_execute1() {
		suite.run("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ;D1 20 ;D2 400 ;D3 8902 ;D4 197281 ;D5 4865609 ;D6 119060324");
	}
	
	//TODO: este set de test no esta en el archivo original, estaria bueno agregar una excepcion cuando falla alguno de los TESTs
	@Test
	public void test_execute2() {
		suite.run("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ;D1 14 ;D2 191 ;D3 2812 ;D4 43238 ;D5 674624 ;D6 11030083");
	}	
}
