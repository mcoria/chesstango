/**
 * 
 */
package uci.protocol;

import java.util.ArrayList;
import java.util.List;

import uci.protocol.imp.requests.GO;
import uci.protocol.imp.requests.ISREADYCMD;
import uci.protocol.imp.requests.POSITION;
import uci.protocol.imp.requests.QUIT;
import uci.protocol.imp.requests.SETOPTION;
import uci.protocol.imp.requests.STOP;
import uci.protocol.imp.requests.UCI;
import uci.protocol.imp.requests.UCINUEWGAME;
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
			case "SETOPTION":
				result = parseSetOption(words);
				break;
			case "UCINUEWGAME":
				result = new UCINUEWGAME();
				break;
			case "POSITION":
				result = parsePosition(words);
				break;				
			case "QUIT":
				result = new QUIT();
				break;
			case "ISREADY":
				result = new ISREADYCMD();
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



	private UCIRequest parseSetOption(String[] words) {
		return new SETOPTION();
	}


	private UCIRequest parseGo(String[] words) {
		return new GO();
	}


	private UCIRequest parsePosition(String[] words) {
		UCIRequest result = null;
		if (words.length > 1) {
			String positionType = words[1].toUpperCase();
			switch (positionType) {
			case "FEN":
				result = parsePositionFEN(words);
				break;
			case "STARTPOS":
				result = parsePositionSTARTPOS(words);
				break;
			default:
				break;
			}
		}
		return result == null ? new Unknown() : result;
	}



	/**
	 * @param words
	 * @return
	 */
	private UCIRequest parsePositionSTARTPOS(String[] words) {
		UCIRequest result = null;
		List<String> moves = new ArrayList<String>();
		if (words.length == 2) {
			result = new POSITION(false, moves);
		} else {
			String movesword = words[2].toUpperCase();
			if("MOVES".equals(movesword) && words.length > 3){
				for (int i = 3; i < words.length; i++) {
					moves.add(words[i]);
				}
				result = new POSITION(false, moves);
			}

		}
		return result == null ? new Unknown() : result;
	}



	/**
	 * @param words
	 * @return
	 */
	private UCIRequest parsePositionFEN(String[] words) {
		// TODO Auto-generated method stub
		return null;
	}

}
