/**
 * 
 */
package uci;

import java.util.Scanner;

import uci.engine.Engine;
import uci.protocol.UCIDecoder;
import uci.protocol.UCIRequest;
import uci.protocol.UCIResponse;
import uci.protocol.UCIResponseMultiple;
import uci.protocol.UCIResponseSingle;

/**
 * @author Mauricio Coria
 *
 */
public class Main {
	private UCIDecoder uciDecoder = new UCIDecoder();
	
	private Engine engine = new Engine();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.mainLoop();
	}

	protected void mainLoop() {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String input = scanner.nextLine();

			UCIRequest uciRequest = uciDecoder.parseInput(input);

			UCIResponse uciResponse = processRequest(uciRequest);

			if (uciResponse != null) {
				processResponse(uciResponse);
			}

		}
		scanner.close();
	}

	private UCIResponse processRequest(UCIRequest uciRequest) {
		return engine.processRequest(uciRequest);
	}

	private void processResponse(UCIResponse uciResponse) {
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
