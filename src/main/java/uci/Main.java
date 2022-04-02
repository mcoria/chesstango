/**
 * 
 */
package uci;

import java.util.Scanner;

import uci.engine.Engine;
import uci.protocol.UCIDecoder;
import uci.protocol.UCIRequest;
import uci.protocol.UCIResponseChannel;
import uci.protocol.UCIResponse;

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
	public void send(UCIResponse response) {
		System.out.println(response.toString());
	}

}
