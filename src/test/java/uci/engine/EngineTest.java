/**
 * 
 */
package uci.engine;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import chess.Game;
import chess.fen.FENEncoder;
import uci.protocol.UCIDecoder;
import uci.protocol.UCIRequest;
import uci.protocol.UCIResponseChannel;
import uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class EngineTest {
	
	private Engine engine;
	
	private final UCIDecoder uciDecoder = new UCIDecoder();
	
	protected UCIResponse response;
	
	@Before
	public void setUp() {
		this.engine = new Engine(new UCIResponseChannel(){
			@Override
			public void send(UCIResponse response) {
				EngineTest.this.response = response;
			}
		});
		this.response = null;
	}

	@Test
	public void tes1t() {
		engine.do_position_startpos(Arrays.asList("e2e4"));
		
		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGeme()));
	}
	
	@Test
	public void tes12() {
		UCIRequest command = uciDecoder.parseInput("position startpos moves e2e4 g8h6 e4e5 e7e6 f2f3 b8c6 b2b4 h6f5 b1c3 c6a5 a2a3 g7g5 d1e2 a7a6 c3d1 c7c6 g2g3 g5g4 f1h3 f5d4 e2f1 d8e7 d1b2 a5c4 h3g4 e8d8 a1b1 c4e3 f1h3 e3d5 g4h5 d4e2 e1f2 d5f4 h3f1 e7c5 b4c5 f4d5 c2c4 d5c7 f1e1 a6a5 h5g4 d7d6 h2h4 b7b6 e5d6 c8a6 b2a4 e2f4 f2f1 h7h5 b1b4 c7d5 d2d3 a6b5 e1e4 f8e7 e4e2 h8h6 e2c2 d5f6 a4b2 h6h8 c2h2 h8h6 b2a4 b6c5 g4f5 f4d3 f5h3 e7f8 f1e2 f8e7 h3f5 f6d5 c1h6 a8c8 h6e3 b5a4 b4b5 e7f6 c4d5 e6f5 e3g5 d3c1 e2e1 c1e2 d5c6 c8b8 c6c7 d8e8 g5h6 e8d7 b5b8 d7e6 b8h8 a4c6 c7c8q");
		
		command.execute(engine);
		
		Assert.assertEquals("2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1", fenCode(engine.getGeme()));
	}
	
	private String fenCode(Game board) {
		FENEncoder coder = new FENEncoder();
		board.getChessPositionReader().constructBoardRepresentation(coder);
		return coder.getResult();
	}	
}
