package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

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
	public UCIRequestType getType() {
		return UCIRequestType.STOP;
	}

	@Override
	public void execute(Engine engine) {
		engine.do_stop();
	}

}
