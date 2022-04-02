/**
 * 
 */
package uci.protocol;

import java.util.ArrayList;
import java.util.List;

import uci.protocol.requests.CmdGo;
import uci.protocol.requests.CmdIsReady;
import uci.protocol.requests.CmdPosition;
import uci.protocol.requests.CmdQuit;
import uci.protocol.requests.CmdSetOption;
import uci.protocol.requests.CmdStop;
import uci.protocol.requests.CmdUci;
import uci.protocol.requests.CmdUciNewGame;
import uci.protocol.requests.CmdUnknown;

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
				result = new CmdUci();
				break;
			case "SETOPTION":
				result = parseSetOption(words);
				break;
			case "UCINUEWGAME":
				result = new CmdUciNewGame();
				break;
			case "POSITION":
				result = parsePosition(words);
				break;				
			case "QUIT":
				result = new CmdQuit();
				break;
			case "ISREADY":
				result = new CmdIsReady();
				break;
			case "GO":
				result = parseGo(words);
				break;
			case "STOP":
				result = new CmdStop();
				break;				
			default:
				result = new CmdUnknown();
				break;
			}
		}
		
		return result;
	}



	private UCIRequest parseSetOption(String[] words) {
		return new CmdSetOption();
	}


	private UCIRequest parseGo(String[] words) {
		return new CmdGo();
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
		return result == null ? new CmdUnknown() : result;
	}



	/**
	 * @param words
	 * @return
	 */
	private UCIRequest parsePositionSTARTPOS(String[] words) {
		UCIRequest result = null;
		List<String> moves = new ArrayList<String>();
		if (words.length == 2) {
			result = new CmdPosition(false, moves);
		} else {
			String movesword = words[2].toUpperCase();
			if("MOVES".equals(movesword) && words.length > 3){
				for (int i = 3; i < words.length; i++) {
					moves.add(words[i]);
				}
				result = new CmdPosition(false, moves);
			}

		}
		return result == null ? new CmdUnknown() : result;
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
