/**
 * 
 */
package chess.uci.engine;

import chess.board.Game;
import chess.board.representations.fen.FENEncoder;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class EngineMainTest {

	private ExecutorService executorService = Executors.newFixedThreadPool(2);


	@After
	public void teardown(){
		executorService.shutdown();
		try {
			boolean terminated = executorService.awaitTermination(2000, TimeUnit.MILLISECONDS);
			if(terminated == false) {
				throw new RuntimeException("El thread no termino");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Test(timeout = 3000)
	public void test_playZonada() throws IOException, InterruptedException {
		PipedOutputStream outputToEngine = new PipedOutputStream();

		PipedInputStream inputFromEngine = new PipedInputStream();

		EngineZonda engine = new EngineZonda();

		EngineMain engineMain = new EngineMain(engine, new PipedInputStream(outputToEngine), new PrintStream(new PipedOutputStream(inputFromEngine),true));

		engineMain.main(executorService);
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
		Thread.sleep(500);
		Assert.assertEquals(EngineZonda.WaitCmdGo.class,  engine.getCurrentState().getClass());

		Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fenCode(engine.getGame()));
		Thread.sleep(500);
		Assert.assertEquals(EngineZonda.WaitCmdGo.class,  engine.getCurrentState().getClass());

		// quit command
		out.println("quit");
	}

	@Test(timeout = 3000)
	public void test_playProxy() throws IOException, InterruptedException {
		PipedOutputStream outputToEngine = new PipedOutputStream();

		EngineProxy engine = new EngineProxy();

		EngineMain engineMain = new EngineMain(engine, new PipedInputStream(outputToEngine), System.out);
		engineMain.main(executorService);

		PrintStream out = new PrintStream(outputToEngine,true);

		// uci command
		out.println("uci");
		Thread.sleep(200);

		// isready command
		out.println("isready");
		Thread.sleep(200);

		// ucinewgame command
		out.println("ucinewgame");
		Thread.sleep(200);

		// isready command
		out.println("isready");
		Thread.sleep(200);

		// isrpositioneady command
		out.println("position startpos moves e2e4");
		Thread.sleep(200);

		// quit command
		out.println("quit");
		Thread.sleep(200);

	}

	private String fenCode(Game board) {
		FENEncoder coder = new FENEncoder();
		board.getChessPosition().constructBoardRepresentation(coder);
		return coder.getResult();
	}

}
