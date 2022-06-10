package chess.uci.protocol.responses.uci;

import chess.uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class RspIdEngineAuthor implements UCIResponse {

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
		return "id author Mauricio Coria";
	}

}
