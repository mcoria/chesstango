package net.chesstango.tools;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


//TODO: Agrupar en una sola clase los tests de https://www.chessprogramming.org/Perft_Results
//TODO: este set de test no esta en el archivo original, estaria bueno agregar una excepcion cuando falla alguno de los TESTs

/**
 * @author Mauricio Coria
 *
 */
public class PerftMainTest {
	
	private PerftMain suite;
	
	@BeforeEach
	public void setUp() {
		//suite = new PerftSuite(new ChessFactoryDebug());
		suite = new PerftMain();
	}
	
	@Test
	public void test_parse0() {
		suite.parseTests("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ;D1 20 ;D2 400 ;D3 8902 ;D4 197281 ;D5 4865609 ;D6 119060324");
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", suite.getFen());
		assertEquals(20, suite.expectedPerftResults[0]);
		assertEquals(400, suite.expectedPerftResults[1]);
		assertEquals(8902, suite.expectedPerftResults[2]);
		assertEquals(197281, suite.expectedPerftResults[3]);
		assertEquals(4865609, suite.expectedPerftResults[4]);
		assertEquals(119060324, suite.expectedPerftResults[5]);
	}
	
	@Test
	public void test_parse1() {
		suite.parseTests("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1 ;D6 824064");
		assertEquals("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1", suite.getFen());
		assertEquals(824064, suite.expectedPerftResults[0]);
	}

	@Test
	public void test_parse2() {
		suite.parseTests("rn2qb1r/N1pkp1pp/Qp6/3bnP2/2pP1P2/4P2P/PP1NB3/R1B1K1R1 w Q - 0 1; D1 45; D2 1457; D3 61279; D4 1916981; D5 79775362; D6 2487489850");
		assertEquals("rn2qb1r/N1pkp1pp/Qp6/3bnP2/2pP1P2/4P2P/PP1NB3/R1B1K1R1 w Q - 0 1", suite.getFen());
		assertEquals(2487489850L, suite.expectedPerftResults[5]);
	}

	@Test
	public void test_parse3() {
		suite.parseTests("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1; D1 48; D2 2039; D3 97862; D4 4085603; D5 193690690; D6 8031647685");
		assertEquals("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", suite.getFen());
		assertEquals(8031647685L, suite.expectedPerftResults[5]);
	}

	@Test
	public void test_parse4() {
		suite.parseTests("8/5kpp/8/8/1p3P2/6PP/r3KP2/1R1q4 w - -  0 1; D4 23441");
		assertEquals("8/5kpp/8/8/1p3P2/6PP/r3KP2/1R1q4 w - -  0 1", suite.getFen());
		assertEquals(23441L, suite.expectedPerftResults[0]);
	}

	
	@Test // 25segs
	public void run_1() {
		assertTrue(suite.run("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1 ;D1 26 ;D2 568 ;D3 13744 ;D4 314346 ;D5 7594526 ;D6 179862938"));
	}
	
	@Test
	public void run_2() {
		assertTrue(suite.run("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1 ;D1 26 ;D2 112 ;D3 3189 ;D4 17945 ;D5 532933 ;D6 2788982"));
	}	
	
	
	@Test
	public void run_3() {
		assertTrue(suite.run("4k2r/6K1/8/8/8/8/8/8 w k - 0 1 ;D1 3 ;D2 32 ;D3 134 ;D4 2073 ;D5 10485 ;D6 179869"));
	}
	
	
	@Test
	public void run_4() {
		assertTrue(suite.run("r3k3/1K6/8/8/8/8/8/8 w q - 0 1 ;D1 4 ;D2 49 ;D3 243 ;D4 3991 ;D5 20780 ;D6 367724"));
	}
	
	@Test
	public void run_5() {
		assertTrue(suite.run("8/8/1k6/8/2pP4/8/5BK1/8 b - d3 0 1 ;D6 824064"));
	}

	@Test
	public void run_6() {
		assertTrue(suite.run("8/5kpp/8/8/1p3P2/6PP/r3KP2/1R1q4 w - -  0 1; D4 23441"));
	}

	@Test
	@Disabled
	public void run_7() {
		assertTrue(suite.run("4r2r/Qppk1n1p/3b1p2/2B2n2/PP3P1q/2p1p1P1/R3P2P/3K1bR1 w - - 0 1; D1 26; D2 1225; D3 32565; D4 1425765; D5 39316149; D6 1650366795"));
	}
}
