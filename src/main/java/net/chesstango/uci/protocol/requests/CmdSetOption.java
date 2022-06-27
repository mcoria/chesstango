package net.chesstango.uci.protocol.requests;

import net.chesstango.uci.protocol.UCIMessageExecutor;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdSetOption implements UCIRequest {

	private final String input;

	public CmdSetOption(String input) {
		this.input = input;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.SETOPTION;
	}


	@Override
	public void execute(UCIMessageExecutor executor) {
		executor.do_setOption(this);
	}

	@Override
	public String toString() {
		return input;
	}
}
