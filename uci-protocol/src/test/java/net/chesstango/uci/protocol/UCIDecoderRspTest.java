package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 *
 */
public class UCIDecoderRspTest {
	
	private UCIDecoder decoder = null;

    @BeforeEach
	public void setUp() {
		decoder  = new UCIDecoder();
	}

	@Test
	public void test_parse_readyok(){
		UCIMessage result =  decoder.parseMessage("readyok");

		assertTrue(result instanceof RspReadyOk);

		assertEquals("readyok", result.toString());
	}

	@Test
	public void test_parse_bestmove(){
		UCIMessage result =  decoder.parseMessage("bestmove a2a4");

		assertTrue(result instanceof RspBestMove);

		RspBestMove bestMove = (RspBestMove) result;

		assertEquals("a2a4", bestMove.getBestMove());

		assertEquals("bestmove a2a4", result.toString());
	}

	@Test
	public void test_parse_bestmove_ponder(){
		UCIMessage result =  decoder.parseMessage("bestmove a2a4 ponder a5a7");

		assertTrue(result instanceof RspBestMove);

		RspBestMove bestMove = (RspBestMove) result;

		assertEquals("a2a4", bestMove.getBestMove());
		assertEquals("a5a7", bestMove.getPonderMove());

		assertEquals("bestmove a2a4 ponder a5a7", result.toString());
	}

	@Test
	public void test_parse_id_author(){
		UCIMessage result =  decoder.parseMessage("id author Mauricio Coria");

		assertTrue(result instanceof RspId);

		RspId rspId = (RspId) result;

		assertEquals(RspId.RspIdType.AUTHOR, rspId.getIdType());

		assertEquals("id author Mauricio Coria", result.toString());
	}

	@Test
	public void test_parse_id_name(){
		UCIMessage result =  decoder.parseMessage("id name Zonda");

		assertTrue(result instanceof RspId);

		RspId rspId = (RspId) result;

		assertEquals(RspId.RspIdType.NAME, rspId.getIdType());

		assertEquals("id name Zonda", result.toString());
	}

	@Test
	public void test_parse_uciok(){
		UCIMessage result =  decoder.parseMessage("uciok");

		assertTrue(result instanceof RspUciOk);

		assertEquals("uciok", result.toString());
	}
}
