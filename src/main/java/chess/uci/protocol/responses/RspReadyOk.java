package chess.uci.protocol.responses;

import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.UCIResponse;

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
	public void execute(UCIMessageExecutor executor) {
		executor.receive_readyOk(this);
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
