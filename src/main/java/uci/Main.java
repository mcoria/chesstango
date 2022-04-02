/**
 * 
 */
package uci;

import java.util.Scanner;

import uci.engine.Engine;
import uci.engine.UCIResponseChannel;
import uci.protocol.UCIDecoder;
import uci.protocol.UCIRequest;
import uci.protocol.UCIResponse;
import uci.protocol.UCIResponseMultiple;
import uci.protocol.UCIResponseSingle;

/**
 * @author Mauricio Coria
 *
 */
public class Main implements UCIResponseChannel {
	private final UCIDecoder uciDecoder = new UCIDecoder();
	
	private final Engine engine;

	public static void main(String[] args) {
		Main main = new Main();
		main.mainLoop();
	}


	public Main() {
		engine = new Engine(this);
	}
	
	protected void mainLoop() {
		Scanner scanner = new Scanner(System.in);
		while (engine.keepProcessing() && scanner.hasNext()) {
			String input = scanner.nextLine();

			UCIRequest uciRequest = uciDecoder.parseInput(input);

			uciRequest.execute(engine);

		}
		scanner.close();
	}

	@Override
	public void send(UCIResponse uciResponse) {
		if (uciResponse instanceof UCIResponseMultiple) {
			UCIResponseMultiple multiline = (UCIResponseMultiple) uciResponse;
			for (UCIResponseSingle singleResponse : multiline) {
				processResponseSingle(singleResponse);
			}
		} else {
			processResponseSingle((UCIResponseSingle) uciResponse);
		}
	}


	private void processResponseSingle(UCIResponseSingle singleResponse) {
		System.out.println(singleResponse.toString());
	}

}
