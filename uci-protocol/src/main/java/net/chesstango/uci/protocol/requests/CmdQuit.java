package net.chesstango.uci.protocol.requests;

import net.chesstango.uci.protocol.UCIMessageExecutor;
import net.chesstango.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdQuit implements UCIRequest {

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.QUIT;
	}


	@Override
	public void execute(UCIMessageExecutor executor) {
		executor.do_quit(this);
	}

	@Override
	public String toString() {
		return "quit";
	}
}
