/**
 * 
 */
package net.chesstango.board.perft;

import static org.junit.Assert.assertEquals;

import net.chesstango.board.Game;
import net.chesstango.board.perft.imp.PerftBrute;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class PerftDifferentPositionsTest extends AbstractPerftTest {

	private Perft pert;

	private Game board;

	@Before
	public void setUp() throws Exception {
		pert = new PerftBrute();
	}


	@Test
	public void test_1() {
		board = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		
		PerftResult result = pert.start(board, 5);
		
		assertEquals(532933, result.getTotalNodes());
	}


	@Test // 12segs
	public void test_2() {
		board = getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

		PerftResult result = pert.start(board, 5);

		assertEquals(193690690, result.getTotalNodes());
	}
}
