/**
 * 
 */
package chess.uci.engine;

import chess.board.Game;
import chess.board.representations.fen.FENEncoder;
import chess.uci.protocol.UCIMessage;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 *
 */
public class MainTest {

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	@Test
	public void test_play() throws IOException {
		//probar que settea input/output y llama a la activacion
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
