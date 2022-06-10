package chess.uci.protocol.responses.uci;

import chess.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class RspUciOk implements UCIResponse {

	@Override
	public MessageType getMessageType() {
		return MessageType.Response;
	}

	@Override
	public UCIResponseType getResponseType() {
		return UCIResponseType.UCIOK;
	}
	

	@Override
	public String toString() {
		return "uciok";
	}

}
