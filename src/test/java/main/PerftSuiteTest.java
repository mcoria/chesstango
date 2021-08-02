package main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

//TODO: Agrupar en una sola clase los tests de https://www.chessprogramming.org/Perft_Results
//TODO: este set de test no esta en el archivo original, estaria bueno agregar una excepcion cuando falla alguno de los TESTs
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
	
	
	//Test
	public void test_from_file() {
		suite.run("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ;D1 20 ;D2 400 ;D3 8902 ;D4 197281 ;D5 4865609 ;D6 119060324");
	}
	
	
}