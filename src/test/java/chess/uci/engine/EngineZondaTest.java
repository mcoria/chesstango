/**
 * 
 */
package chess.uci.engine;

import chess.board.Game;
import chess.board.representations.fen.FENEncoder;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.stream.UCIOutputStreamAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class EngineZondaTest {
	private EngineZonda engine;

	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	
	@Before
	public void setUp() {
		engine = new EngineZonda(executorService);
	}

	//TODO: No en todos los tests se llama a la operacion de shutdown()
	public void teardown(){
		try {
			boolean terminated = executorService.awaitTermination(2000, TimeUnit.MILLISECONDS);
			if(terminated == false) {
				throw new RuntimeException("El thread no termino");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test1_execute_position_startpos_01() {
		engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(System.out)));

		engine.write(new CmdUci());
		engine.write(new CmdUciNewGame());
		engine.write(new CmdPosition(Arrays.asList("e2e4")));
		
		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGame()));
	}
	
	@Test
	public void test1_execute_position_startpos_02() {
		engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(System.out)));
		engine.write(new CmdUci());
		engine.write(new CmdUciNewGame());
		engine.write(new CmdPosition(Arrays.asList("e2e4","g8h6","e4e5","e7e6","f2f3","b8c6","b2b4","h6f5","b1c3","c6a5","a2a3","g7g5","d1e2","a7a6","c3d1","c7c6","g2g3","g5g4","f1h3","f5d4","e2f1","d8e7","d1b2","a5c4","h3g4","e8d8","a1b1","c4e3","f1h3","e3d5","g4h5","d4e2","e1f2","d5f4","h3f1","e7c5","b4c5","f4d5","c2c4","d5c7","f1e1","a6a5","h5g4","d7d6","h2h4","b7b6","e5d6","c8a6","b2a4","e2f4","f2f1","h7h5","b1b4","c7d5","d2d3","a6b5","e1e4","f8e7","e4e2","h8h6","e2c2","d5f6","a4b2","h6h8","c2h2","h8h6","b2a4","b6c5","g4f5","f4d3","f5h3","e7f8","f1e2","f8e7","h3f5","f6d5","c1h6","a8c8","h6e3","b5a4","b4b5","e7f6","c4d5","e6f5","e3g5","d3c1","e2e1","c1e2","d5c6","c8b8","c6c7","d8e8","g5h6","e8d7","b5b8","d7e6","b8h8","a4c6","c7c8q")));
		
		Assert.assertEquals("2Q4R/5p2/2bPkb1B/p1p2p1p/7P/P4PP1/4n2Q/4K1NR b - - 0 1", fenCode(engine.getGame()));
	}
	
	@Test
	public void test1_execute_position_fen() {
		engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(System.out)));

		engine.write(new CmdUci());
		engine.write(new CmdUciNewGame());
		engine.write(new CmdPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", Arrays.asList("e2e4") ));
		
		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGame()));
	}


	@Test
	public void test_play() throws IOException, InterruptedException {
		PipedOutputStream posOutput = new PipedOutputStream();
		PipedInputStream pisOutput = new PipedInputStream(posOutput);

		engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(new PrintStream(posOutput,true))));

		BufferedReader in = new BufferedReader(new InputStreamReader(pisOutput));

		// Initial state
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// uci command
		engine.write(new CmdUci());
		Assert.assertEquals("id name Zonda", in.readLine());
		Assert.assertEquals("id author Mauricio Coria", in.readLine());
		Assert.assertEquals("uciok", in.readLine());
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// isready command
		engine.write(new CmdIsReady());
		Assert.assertEquals("readyok", in.readLine());
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// ucinewgame command
		engine.write(new CmdUciNewGame());
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// isready command
		engine.write(new CmdIsReady());
		Assert.assertEquals("readyok", in.readLine());
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// position command
		engine.write(new CmdPosition(Arrays.asList("e2e4") ));
		Assert.assertEquals(EngineZonda.WaitCmdGo.class,  engine.getCurrentState().getClass());
		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGame()));
		Thread.sleep(200);
		Assert.assertEquals(EngineZonda.WaitCmdGo.class,  engine.getCurrentState().getClass());

		// quit command
		engine.write(new CmdQuit());

		teardown();
	}

	private String fenCode(Game board) {
		FENEncoder coder = new FENEncoder();
		board.getChessPosition().constructBoardRepresentation(coder);
		return coder.getResult();
	}	
}
