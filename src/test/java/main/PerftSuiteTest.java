package main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

//TODO: Agrupar en una sola clase los tests de https://www.chessprogramming.org/Perft_Results
//TODO: este set de test no esta en el archivo original, estaria bueno agregar una excepcion cuando falla alguno de los TESTs

/**
 * @author Mauricio Coria
 *
 */
public class PerftSuiteTest {
	
	private PerftSuite suite;
	
	@Before
	public void setUp() {
		//suite = new PerftSuite(new DebugChessFactory());
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
		suite.run("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1 ;D1 26 ;D2 568 ;D3 13744 ;D4 314346 ;D5 7594526 ;D6 179862938");
	}
	
	
}
