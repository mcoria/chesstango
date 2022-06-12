package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponse;
import chess.uci.ui.EngineClient;
import chess.uci.ui.EngineClientResponseListener;

/**
 * @author Mauricio Coria
 *
 */
public class RspReadyOk implements UCIResponse {


	@Override
	public MessageType getMessageType() {
		return MessageType.Response;
	}

	@Override
	public void execute(EngineClientResponseListener engineClient) {
		engineClient.receive_readyOk(this);
	}

	@Override
	public UCIResponseType getResponseType() {
		return UCIResponseType.READYOK;
	}


	@Override
	public String toString() {
		return "readyok";
	}

}
