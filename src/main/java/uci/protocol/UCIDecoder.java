/**
 * 
 */
package uci.protocol;

import uci.protocol.imp.requests.ISREADYCMD;
import uci.protocol.imp.requests.QUIT;
import uci.protocol.imp.requests.STOP;
import uci.protocol.imp.requests.UCI;
import uci.protocol.imp.requests.Unknown;

/**
 * @author Mauricio Coria
 *
 */
public class UCIDecoder {

	/**
	 * @param input
	 * @return
	 */
	public UCIRequest parseInput(String input) {
		UCIRequest result = null;
		
		String[] words = input.split("\\W+");
		
		if(words.length > 0){
			String command = words[0].toUpperCase();
			switch (command) {
			case "UCI":
				result = new UCI();
				break;
			case "QUIT":
				result = new QUIT();
				break;
			case "ISREADY":
				result = new ISREADYCMD();
				break;
			case "POSITION":
				result = parsePosition(words);
				break;
			case "GO":
				result = parseGo(words);
				break;
			case "STOP":
				result = new STOP();
				break;				
			default:
				result = new Unknown();
				break;
			}
		}
		
		return result;
	}


	private UCIRequest parseGo(String[] words) {
		return new GO();
	}


	private UCIRequest parsePosition(String[] words) {
		// TODO Auto-generated method stub
		return null;
	}

}
