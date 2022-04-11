/**
 * 
 */
package chess.board.perft;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.board.Game;
import chess.board.perft.imp.PerftBrute;

/**
 * @author Mauricio Coria
 *
 */
public class PerftTests extends AbstractPerftTest {

	@Test
	public void test_1() {
		Perft pert = new PerftBrute();
		Game board = getGame("4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		
		PerftResult result = pert.start(board, 5);
		
		assertEquals(532933, result.getTotalNodes());
	}
	
}
