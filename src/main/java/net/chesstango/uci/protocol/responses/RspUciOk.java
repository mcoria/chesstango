package net.chesstango.uci.protocol.responses;

import net.chesstango.uci.protocol.UCIMessageExecutor;
import net.chesstango.uci.protocol.UCIResponse;

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
	public void execute(UCIMessageExecutor executor) {
		executor.receive_uciOk(this);
	}

	@Override
	public String toString() {
		return "uciok";
	}

}
