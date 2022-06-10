/**
 * 
 */
package chess.uci.protocol;

import chess.uci.protocol.requests.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class UCIDecoder {

	/**
	 * @param input
	 * @return
	 */
	public UCIMessage parseMessage(String input) {
		UCIMessage result = null;
		
		String[] words = input.split(" ");
		
		if(words.length > 0){
			String command = words[0].toUpperCase();
			switch (command) {
			case "UCI":
				result = new CmdUci();
				break;
			case "SETOPTION":
				result = parseSetOption(words);
				break;
			case "UCINEWGAME":
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
				result = new UCIMessageUnknown(input);
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


	private UCIMessage parsePosition(String[] words) {
		UCIMessage result = null;
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
		return result == null ? new UCIMessageUnknown(words.toString()) : result;
	}

	private UCIMessage parsePositionSTARTPOS(String[] words) {
		UCIMessage result = null;
		List<String> moves = new ArrayList<String>();
		if (words.length == 2) {
			result = new CmdPositionStart(moves);
		} else {
			String movesword = words[2].toUpperCase();
			if ("MOVES".equals(movesword) && words.length > 3) {
				for (int i = 3; i < words.length; i++) {
					moves.add(words[i]);
				}
				result = new CmdPositionStart(moves);
			}

		}
		return result == null ? new UCIMessageUnknown(words.toString()) : result;
	}


	private UCIRequest parsePositionFEN(String[] words) {
		boolean readingFen = true;
		String fenString = "";
		List<String> moves = new ArrayList<String>();
		for (int i = 2; i < words.length; i++) {
			if (readingFen) {
				if ("MOVES".equalsIgnoreCase(words[i])) {
					readingFen = false;
				} else {
					if (fenString.length() == 0) {
						fenString = fenString.concat(words[i]);
					} else {
						fenString = fenString.concat(" " + words[i]);
					}
				}
			} else {
				moves.add(words[i]);
			}
		}

		return new CmdPositionFen(fenString, moves);
	}

}
