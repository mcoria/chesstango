package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdUci implements UCIRequest {

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.UCI;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_uci(this);
	}

	@Override
	public String toString() {
		return "uci";
	}
}
