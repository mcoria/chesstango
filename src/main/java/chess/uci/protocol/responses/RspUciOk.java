package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponse;
import chess.uci.ui.EngineClient;
import chess.uci.ui.EngineClientResponseListener;

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
	public void execute(EngineClientResponseListener engineClient) {
		engineClient.receive_uciOk(this);
	}

	@Override
	public String toString() {
		return "uciok";
	}

}
