/**
 * 
 */
package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.requests.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class UCIDecoderCmdTest {
	
	private UCIDecoder decoder = null;

	@Before
	public void setUp() {
		decoder  = new UCIDecoder();
	}

	@Test
	public void test_parse_position_startpos() {
		UCIMessage result =  decoder.parseMessage("position startpos");

		Assert.assertTrue(result instanceof UCIRequest);

		Assert.assertTrue(result instanceof CmdPosition);

		CmdPosition command = (CmdPosition) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(0, moves.size());

		Assert.assertEquals("position startpos", result.toString());
	}
	
	
	@Test
	public void test_parse_postition_startpos_with_1_move() {
		UCIMessage result = decoder.parseMessage("position startpos moves f2f4");

		Assert.assertTrue(result instanceof CmdPosition);

		CmdPosition command = (CmdPosition) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(1, moves.size());
		
		Assert.assertEquals("f2f4", moves.get(0));

		Assert.assertEquals("position startpos moves f2f4", result.toString());
	}	
	
	@Test
	public void test_parse_postition_startpos_with_2_move() {
		UCIMessage result = decoder.parseMessage("position startpos moves e2e3 e7e5");

		Assert.assertTrue(result instanceof CmdPosition);

		CmdPosition command = (CmdPosition) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(2, moves.size());
		
		Assert.assertEquals("e2e3", moves.get(0));
		Assert.assertEquals("e7e5", moves.get(1));

		Assert.assertEquals("position startpos moves e2e3 e7e5", result.toString());
	}

	
	@Test
	public void test_parse_postition_fen_with_1_move() {
		UCIMessage result =  decoder.parseMessage("position fen 2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1 moves e2e4");
		
		Assert.assertTrue(result instanceof CmdPosition);
		
		CmdPosition command = (CmdPosition) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(1, moves.size());
		Assert.assertEquals("2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1", command.getFen());
		Assert.assertEquals("e2e4", moves.get(0));

		Assert.assertEquals("position fen 2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1 moves e2e4", result.toString());
	}

	@Test
	public void test_parse_uci() {
		UCIMessage result =  decoder.parseMessage("uci");

		Assert.assertTrue(result instanceof CmdUci);

		Assert.assertEquals("uci", result.toString());
	}

	@Test
	public void test_parse_ucinewgame() {
		UCIMessage result =  decoder.parseMessage("ucinewgame");

		Assert.assertTrue(result instanceof CmdUciNewGame);

		Assert.assertEquals("ucinewgame", result.toString());
	}

	@Test
	public void test_parse_stop() {
		UCIMessage result =  decoder.parseMessage("stop");

		Assert.assertTrue(result instanceof CmdStop);

		Assert.assertEquals("stop", result.toString());
	}

	@Test
	public void test_parse_quit() {
		UCIMessage result =  decoder.parseMessage("quit");

		Assert.assertTrue(result instanceof CmdQuit);

		Assert.assertEquals("quit", result.toString());
	}

	@Test
	public void test_parse_go() {
		UCIMessage result =  decoder.parseMessage("go");

		Assert.assertTrue(result instanceof CmdGo);
		CmdGo go = (CmdGo) result;
		Assert.assertEquals(CmdGo.GoType.NO_SUBCOMMAND, go.getGoType());

		Assert.assertEquals("go", result.toString());
	}

	@Test
	public void test_parse_go_infinite() {
		UCIMessage result =  decoder.parseMessage("go infinite");

		Assert.assertTrue(result instanceof CmdGo);
		CmdGo go = (CmdGo) result;
		Assert.assertEquals(CmdGo.GoType.INFINITE, go.getGoType());

		Assert.assertEquals("go infinite", result.toString());
	}


	@Test
	public void test_parse_go_depth() {
		UCIMessage result =  decoder.parseMessage("go depth 1");

		Assert.assertTrue(result instanceof CmdGo);

		CmdGo go = (CmdGo) result;
		Assert.assertEquals(CmdGo.GoType.DEPTH, go.getGoType());
		Assert.assertEquals(1, go.getDepth());

		Assert.assertEquals("go depth 1", result.toString());
	}

	@Test
	public void test_parse_ready() {
		UCIMessage result =  decoder.parseMessage("isready");

		Assert.assertTrue(result instanceof CmdIsReady);

		Assert.assertEquals("isready", result.toString());
	}

	@Test
	public void test_parse_setoption() {
		UCIMessage result =  decoder.parseMessage("setoption");

		Assert.assertTrue(result instanceof CmdSetOption);

		Assert.assertEquals("setoption", result.toString());
	}
}
