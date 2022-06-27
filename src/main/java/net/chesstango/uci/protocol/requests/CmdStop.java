package net.chesstango.uci.protocol.requests;

import net.chesstango.uci.protocol.UCIMessageExecutor;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdStop implements UCIRequest {

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.STOP;
	}

	@Override
	public void execute(UCIMessageExecutor executor) {
		executor.do_stop(this);
	}

	@Override
	public String toString() {
		return "stop";
	}
}
