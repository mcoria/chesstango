package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

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
	public void execute(Engine engine) {
		engine.do_setOption(this);
	}

	@Override
	public String toString() {
		return input;
	}
}
