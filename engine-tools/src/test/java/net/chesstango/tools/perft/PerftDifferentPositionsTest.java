package net.chesstango.tools.perft;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * @author Mauricio Coria
 *
 */
public class PerftDifferentPositionsTest extends AbstractPerftTest {

	private Perft perft;

	private Game game;

	@BeforeEach
	public void setUp() throws Exception {
		perft = createPerft();
	}


	@Test
	public void test01() {
		game = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		
		PerftResult result = perft.start(game, 5);
		
		assertEquals(532933, result.getTotalNodes());
	}


	@Test
	public void test02() {
		game = getGame("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1");

		PerftResult result = perft.start(game, 5);

		assertEquals(46934059, result.getTotalNodes());
	}

	@Test
	@Disabled
	public void test03() {
		game = getGame("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1");

		PerftResult result = perft.start(game, 6);

		assertEquals(1478670842, result.getTotalNodes());
	}

	@Test
	public void test04() {
		game = getGame("r3k2r/p1pp1pb1/bn1qpnpB/3PN3/1p2P3/2N2Q1p/PPP1BPPP/R2K3R b kq - 3 2");

		game.executeMove(Square.c7, Square.c5);

		PerftResult result = perft.start(game, 1);

		assertFalse(result.moveExists(Square.d5 , Square.c6 ));	// En Passant capture deja el rey al descubierto

		assertEquals(44, result.getMovesCount());
		assertEquals(44, result.getTotalNodes());
	}

}
