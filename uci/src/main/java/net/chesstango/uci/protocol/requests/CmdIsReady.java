package net.chesstango.uci.protocol.requests;

import net.chesstango.uci.protocol.UCIMessageExecutor;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdIsReady implements UCIRequest {


	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.ISREADY;
	}


	@Override
	public void execute(UCIMessageExecutor executor) {
		executor.do_isReady(this);
	}

	@Override
	public String toString() {
		return "isready";
	}
}
