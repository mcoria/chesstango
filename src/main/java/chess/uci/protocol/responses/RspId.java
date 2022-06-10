package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class RspId implements UCIResponse {

	private final String text;

	public RspId(String text) {
		this.text = text;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Response;
	}

	@Override
	public UCIResponseType getResponseType() {
		return UCIResponseType.ID;
	}


	@Override
	public String toString() {
		return "id " + text;
	}

}
