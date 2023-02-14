/**
 * 
 */
package net.chesstango.uci.protocol;

import net.chesstango.uci.protocol.requests.*;
import net.chesstango.uci.protocol.responses.RspBestMove;
import net.chesstango.uci.protocol.responses.RspId;
import net.chesstango.uci.protocol.responses.RspReadyOk;
import net.chesstango.uci.protocol.responses.RspUciOk;

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

			// ====================== REQUESTS
			case "UCI":
				result = new CmdUci();
				break;
			case "SETOPTION":
				result = parseSetOption(input);
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

			// ====================== RESPONSES
			case "READYOK":
				result = new RspReadyOk();
				break;
			case "UCIOK":
				result = new RspUciOk();
				break;
			case "BESTMOVE":
				result = parseBestMove(words);
				break;
			case "ID":
				result = parseId(words);
				break;

			// ====================== UNKNOWN
			default:
				result = new UCIMessageUnknown(input);
				break;
			}
		}
		return result;
	}

	private UCIMessage parseId(String[] words) {
		UCIMessage result = null;
		if(words.length > 2) {
			String typeStr = words[1].toUpperCase();
			RspId.RspIdType type = null;
			if(RspId.RspIdType.AUTHOR.toString().equalsIgnoreCase(typeStr)){
				type = RspId.RspIdType.AUTHOR;
			} else if(RspId.RspIdType.NAME.toString().equalsIgnoreCase(typeStr)){
				type = RspId.RspIdType.NAME;
			}

			if(type != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < words.length; i++) {
						sb.append(words[i]);
						if (i < words.length - 1) {
							sb.append(" ");
						}
					}

				result = new RspId(type, sb.toString());
			}
		}
		return result == null ? new UCIMessageUnknown(words.toString()) : result;
	}

	private UCIMessage parseBestMove(String[] words) {
		String bestMove = words[1];
		String ponderMove = null;
		if(words.length == 4){
			if("ponder".equalsIgnoreCase(words[2])){
				ponderMove = words[3];
			}
		}
		return new RspBestMove(bestMove, ponderMove);
	}


	private UCIMessage parseSetOption(String input) {
		return new CmdSetOption(input);
	}


	private UCIMessage parseGo(String[] words) {
		CmdGo result = null;
		if (words.length > 1) {
			String goType = words[1].toUpperCase();
			switch (goType) {
				case "INFINITE":
					result = new CmdGo();
					result.setGoType(CmdGo.GoType.INFINITE);
					break;
				case "DEPTH":
					result = parseGoDepth(words);
					break;
				default:
					break;
			}
		} else {
			result = new CmdGo();
		}
		return result == null ? new UCIMessageUnknown(words.toString()) : result;
	}

	private CmdGo parseGoDepth(String[] words) {
		CmdGo result = new CmdGo();

		String depth = words[2].toUpperCase();
		int depthInt = Integer.parseInt(depth);

		result.setGoType(CmdGo.GoType.DEPTH);
		result.setDepth(depthInt);

		return result;
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
		List<String> moves = new ArrayList<String>();
		if (words.length  > 2) {
			String movesword = words[2].toUpperCase();
			if ("MOVES".equals(movesword) && words.length > 3) {
				for (int i = 3; i < words.length; i++) {
					moves.add(words[i]);
				}
			}

		}
		return new CmdPosition(moves);
	}


	private UCIMessage parsePositionFEN(String[] words) {
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

		return new CmdPosition(fenString, moves);
	}

}