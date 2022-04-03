/**
 * 
 */
package uci.protocol;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uci.protocol.requests.CmdPositionFen;
import uci.protocol.requests.CmdPositionStart;

/**
 * @author Mauricio Coria
 *
 */
public class UCIDecoderTest {
	
	private UCIDecoder decoder = null;
			
	@Before
	public void setUp() {
		decoder  = new UCIDecoder();
	}

	@Test
	public void tes1t() {
		UCIRequest result = decoder.parseInput("position startpos");
		Assert.assertTrue(result instanceof CmdPositionStart);
		
		CmdPositionStart command = (CmdPositionStart) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(0, moves.size());
	}
	
	
	@Test
	public void test2() {
		UCIRequest result = decoder.parseInput("position startpos moves f2f4");
		Assert.assertTrue(result instanceof CmdPositionStart);
		
		CmdPositionStart command = (CmdPositionStart) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(1, moves.size());
		
		Assert.assertEquals("f2f4", moves.get(0));
	}	
	
	@Test
	public void test3() {
		UCIRequest result = decoder.parseInput("position startpos moves e2e3 e7e5");
		Assert.assertTrue(result instanceof CmdPositionStart);
		
		CmdPositionStart command = (CmdPositionStart) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(2, moves.size());
		
		Assert.assertEquals("e2e3", moves.get(0));
		Assert.assertEquals("e7e5", moves.get(1));
	}
	
	@Test
	public void test4() {
		UCIRequest result = decoder.parseInput("position startpos moves e2e4");
		Assert.assertTrue(result instanceof CmdPositionStart);
		
		CmdPositionStart command = (CmdPositionStart) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(1, moves.size());
		
		Assert.assertEquals("e2e4", moves.get(0));
	}	
	
	@Test
	public void parseFen() {
		UCIRequest result = decoder.parseInput("position fen 2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1 moves e2e4");
		
		Assert.assertTrue(result instanceof CmdPositionFen);
		
		CmdPositionFen command = (CmdPositionFen) result;
		
		List<String> moves = command.getMoves();
		
		Assert.assertEquals(1, moves.size());
		Assert.assertEquals("2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1", command.fen());
		Assert.assertEquals("e2e4", moves.get(0));
	}	
	
}
