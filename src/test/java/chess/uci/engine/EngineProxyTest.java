/**
 * 
 */
package chess.uci.engine;

import chess.uci.protocol.UCIDecoder;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.stream.UCIOutputStreamAdapter;
import org.junit.*;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class EngineProxyTest {
	private final UCIDecoder uciDecoder = new UCIDecoder();

	private EngineProxy engine;

	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	
	@Before
	public void setUp() {
		this.engine = new EngineProxy();
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
	@Ignore
	public void test_play() throws IOException, InterruptedException {
		PipedOutputStream posOutput = new PipedOutputStream();
		PipedInputStream pisOutput = new PipedInputStream(posOutput);

		engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(new PrintStream(posOutput,true))));

		executorService.execute(engine);

		BufferedReader in = new BufferedReader(new InputStreamReader(pisOutput));

		Assert.assertEquals("Spike 1.4 (Build 84) by Volker Boehm & Ralf Schaefer, Book by Timo Klaustermeyer", in.readLine());

		// uci command
		engine.write(new CmdUci());
		Assert.assertEquals("id name Zonda", in.readLine());
		Assert.assertEquals("id author Mauricio Coria", in.readLine());
		Assert.assertEquals("uciok", in.readLine());

		// isready command
		engine.write(new CmdIsReady());
		Assert.assertEquals("readyok", in.readLine());

		// ucinewgame command
		engine.write(new CmdUciNewGame());

		// isready command
		engine.write(new CmdIsReady());
		Assert.assertEquals("readyok", in.readLine());

		// startpos command
		engine.write(new CmdPosition(Arrays.asList("e2e4") ));
		Thread.sleep(200);

		// quit command
		engine.write(new CmdQuit());
	}

}
