/**
 * 
 */
package chess.board.perft;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.board.Game;
import chess.board.perft.imp.PerftBrute;

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
	
}
