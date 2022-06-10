package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdSetOption implements UCIRequest {

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.SETOPTION;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_setOptions();
	}

}
