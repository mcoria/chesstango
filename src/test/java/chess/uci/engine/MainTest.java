/**
 * 
 */
package chess.uci.engine;

import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.requests.CmdIsReady;
import chess.uci.protocol.requests.CmdPosition;
import chess.uci.protocol.requests.CmdUci;
import chess.uci.protocol.requests.CmdUciNewGame;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 *
 */
public class MainTest {

	@Test
	public void test_readMessage() throws IOException {
		PipedInputStream pis = new PipedInputStream();
		PipedOutputStream pos = new PipedOutputStream(pis);

		Engine engine = mock(Engine.class);

		Main main = new Main(engine, System.out, pis);

		PrintStream out = new PrintStream(pos);
		out.println("uci");
		out.println("isready");
		out.println("ucinewgame");
		out.println("isready");
		out.println("position startpos moves e2e4");

		UCIMessage message = null;

		/*
		message = main.readMessage();
		Assert.assertTrue(message instanceof CmdUci);

		message = main.readMessage();
		Assert.assertTrue(message instanceof CmdIsReady);

		message = main.readMessage();
		Assert.assertTrue(message instanceof CmdUciNewGame);

		message = main.readMessage();
		Assert.assertTrue(message instanceof CmdIsReady);

		message = main.readMessage();
		Assert.assertTrue(message instanceof CmdPosition);
		 */

	}

	@Test
	public void test_writeMessage() throws IOException {
		PipedInputStream pis = new PipedInputStream();
		PipedOutputStream pos = new PipedOutputStream(pis);

		Engine engine = mock(Engine.class);

		Main main = new Main(engine, new PrintStream(pos), System.in);

		/*
		main.writeMessage(new CmdUci());
		main.writeMessage(new CmdIsReady());
		main.writeMessage(new CmdUciNewGame());
		main.writeMessage(new CmdIsReady());
		*/

		BufferedReader reader = new BufferedReader(new InputStreamReader(pis));
		String message = null;

		message = reader.readLine();
		Assert.assertEquals("uci", message);

		message = reader.readLine();
		Assert.assertEquals("isready", message);

		message = reader.readLine();
		Assert.assertEquals("ucinewgame", message);

		message = reader.readLine();
		Assert.assertEquals("isready", message);

	}

}
