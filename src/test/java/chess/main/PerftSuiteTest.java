package chess.main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chess.main.PerftSuite;

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
	public void test_parse0() {
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
	public void test_parse1() {
		suite.parseTests("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1 ;D6 824064");
		
		assertEquals("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1", suite.fen);
		
		assertEquals(824064, suite.perftResults[0]);
		
	}	
	
	
	@Test
	public void test_1() {
		assertTrue(suite.run("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1 ;D1 26 ;D2 568 ;D3 13744 ;D4 314346 ;D5 7594526 ;D6 179862938"));
	}
	
	
	@Test
	public void test_2() {
		assertTrue(suite.run("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1 ;D1 26 ;D2 112 ;D3 3189 ;D4 17945 ;D5 532933 ;D6 2788982"));
	}	
	
	
	@Test
	public void test_3() {
		assertTrue(suite.run("4k2r/6K1/8/8/8/8/8/8 w k - 0 1 ;D1 3 ;D2 32 ;D3 134 ;D4 2073 ;D5 10485 ;D6 179869"));
	}
	
	
	@Test
	public void test_4() {
		assertTrue(suite.run("r3k3/1K6/8/8/8/8/8/8 w q - 0 1 ;D1 4 ;D2 49 ;D3 243 ;D4 3991 ;D5 20780 ;D6 367724"));
	}
	
	@Test
	public void test_5() {
		assertTrue(suite.run("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1 ;D6 824064"));
	}	
}
