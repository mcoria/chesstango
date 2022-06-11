/**
 * 
 */
package chess.uci.protocol;

import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspUciOk;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class UCIDecoder02Test {
	
	private UCIDecoder decoder = null;

    @Before
	public void setUp() {
		decoder  = new UCIDecoder();
	}

	@Test
	public void test_parse_readyok(){
		UCIMessage result =  decoder.parseMessage("readyok");

		Assert.assertTrue(result instanceof RspReadyOk);

		Assert.assertEquals("readyok", result.toString());
	}

	@Test
	public void test_parse_bestmove(){
		UCIMessage result =  decoder.parseMessage("bestmove a2a4");

		Assert.assertTrue(result instanceof RspBestMove);

		Assert.assertEquals("bestmove a2a4", result.toString());
	}

	@Test
	public void test_parse_id(){
		UCIMessage result =  decoder.parseMessage("id author Mauricio Coria");

		Assert.assertTrue(result instanceof RspId);

		Assert.assertEquals("id author Mauricio Coria", result.toString());
	}

	@Test
	public void test_parse_uciok(){
		UCIMessage result =  decoder.parseMessage("uciok");

		Assert.assertTrue(result instanceof RspUciOk);

		Assert.assertEquals("uciok", result.toString());
	}
}
