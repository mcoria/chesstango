package net.chesstango.board.perft;

import net.chesstango.board.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 *
 */
public class PerftDifferentPositionsTest extends AbstractPerftTest {

	private Perft perft;

	private Game board;

	@BeforeEach
	public void setUp() throws Exception {
		perft = createPerft();
	}


	@Test
	public void test_1() {
		board = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		
		PerftResult result = perft.start(board, 5);
		
		assertEquals(532933, result.getTotalNodes());
	}


	@Test // 12segs
	public void test_2() {
		board = getGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");

		PerftResult result = perft.start(board, 5);

		assertEquals(193690690, result.getTotalNodes());
	}


	@Test
	public void test_3() {
		board = getGame("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1");

		PerftResult result = perft.start(board, 5);

		assertEquals(46934059, result.getTotalNodes());
	}

	@Test
	public void test_4() {
		board = getGame("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1");

		PerftResult result = perft.start(board, 6);

		assertEquals(1478670842, result.getTotalNodes());
	}

}
