package chess.uci;

import java.util.Scanner;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIDecoder;
import chess.uci.protocol.UCIRequest;
import chess.uci.protocol.UCIResponse;
import chess.uci.protocol.UCIResponseChannel;

/**
 * @author Mauricio Coria
 *
 */
public class Main implements UCIResponseChannel {
	private final UCIDecoder uciDecoder = new UCIDecoder();
	
	private final Engine engine;

	private boolean keepProcessing = true;

	public static void main(String[] args) {
		Main main = new Main();
		main.mainLoop();
	}


	public Main() {
		engine = new Engine(this);
	}

	@Override
	public void send(UCIResponse response) {
		System.out.println(response.toString());
	}

	@Override
	public void close() {
		keepProcessing = false;
	}

	protected void mainLoop() {
		Scanner scanner = new Scanner(System.in);
		while (keepProcessing && scanner.hasNext()) {
			String input = scanner.nextLine();

			processInput(input);
		}
		scanner.close();
	}


	protected void processInput(String input) {
		UCIRequest uciRequest = uciDecoder.parseInput(input);

		uciRequest.execute(engine);
	}

	
	protected Engine getEngine(){
		return engine;
	}

}
