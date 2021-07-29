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
	
	
	@Test
	public void test_from_file() {
		suite.run("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ;D1 20 ;D2 400 ;D3 8902 ;D4 197281 ;D5 4865609 ;D6 119060324");
	}
	

	@Test
	public void test_position3() {
		suite.run("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ;D1 14 ;D2 191 ;D3 2812 ;D4 43238 ;D5 674624 ;D6 11030083");
	}
	
	@Test
	public void test_position4() {
		suite.run("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1 ;D1 6 ;D2 264 ;D3 9467 ;D4 422333 ;D5 15833292 ;D6 706045033");
	}
	
	@Test
	public void test_position4_mirror() {
		suite.run("r2q1rk1/pP1p2pp/Q4n2/bbp1p3/Np6/1B3NBn/pPPP1PPP/R3K2R b KQ - 0 1  ;D1 6 ;D2 264 ;D3 9467 ;D4 422333 ;D5 15833292 ;D6 706045033");
	}
	
	@Test
	public void test_position5() {
		suite.run("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 ;D1 44 ;D2 1486 ;D3 62379 ;D4 2103487 ;D5 89941194");
	}
	
	//TODO: Position 6: D6 no puede ser agregado https://www.chessprogramming.org/Perft_Results#Position_6
	@Test
	public void test_position6() {
		suite.run("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ;D1 46 ;D2 2079 ;D3 89890 ;D4 3894594 ;D5 164075551");
	}	
}
