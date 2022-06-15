/**
 * 
 */
package chess.uci.engine;

import chess.board.Game;
import chess.board.representations.fen.FENEncoder;
import chess.uci.protocol.stream.UCIInputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStreamAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 *
 */
public class MainTest {

	private ExecutorService executorService = Executors.newSingleThreadExecutor();


	@Test
	public void test_play() throws IOException, InterruptedException {
		PipedOutputStream outputToEngine = new PipedOutputStream();

		PipedInputStream inputFromEngine = new PipedInputStream();

		EngineZonda engine = new EngineZonda();

		Main main = new Main(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine),true));

		executorService.execute(main::main);
		PrintStream out = new PrintStream(outputToEngine,true);
		BufferedReader in = new BufferedReader(new InputStreamReader(inputFromEngine));

		// is in ready state
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// uci command
		out.println("uci");
		Assert.assertEquals("id name Zonda", in.readLine());
		Assert.assertEquals("id author Mauricio Coria", in.readLine());
		Assert.assertEquals("uciok", in.readLine());
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// isready command
		out.println("isready");
		Assert.assertEquals("readyok", in.readLine());

		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// ucinewgame command
		out.println("ucinewgame");
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// isready command
		out.println("isready");
		Assert.assertEquals("readyok", in.readLine());
		Assert.assertEquals(EngineZonda.Ready.class,  engine.getCurrentState().getClass());

		// isrpositioneady command
		out.println("position startpos moves e2e4");
		Thread.sleep(200);
		Assert.assertEquals(EngineZonda.WaitCmdGo.class,  engine.getCurrentState().getClass());

		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGame()));
		Thread.sleep(200);
		Assert.assertEquals(EngineZonda.WaitCmdGo.class,  engine.getCurrentState().getClass());

		// quit command
		out.println("quit");

		executorService.shutdown();
		boolean terminated = executorService.awaitTermination(2000, TimeUnit.MILLISECONDS);

		Assert.assertTrue("El thread no termino", terminated);
	}

	private void runMainLoop(Main main) {
		executorService.execute(main::main);
	}

	private String fenCode(Game board) {
		FENEncoder coder = new FENEncoder();
		board.getChessPositionReader().constructBoardRepresentation(coder);
		return coder.getResult();
	}

}
