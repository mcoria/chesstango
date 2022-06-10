package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponse;
/**
 * @author Mauricio Coria
 *
 */
public class RspBestMove implements UCIResponse {
	
	private final String bestMove;
	
	public RspBestMove(String bestMove) {
		this.bestMove = bestMove;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Response;
	}

	@Override
	public UCIResponseType getResponseType() {
		return UCIResponseType.BESTMOVE;
	}

	@Override
	public String toString() {
		return "bestmove " + bestMove;
	}

}
