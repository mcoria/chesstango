package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdGo implements UCIRequest {


	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.GO;
	}


	@Override
	public void execute(UCIMessageExecutor executor) {
		executor.do_go(this);
	}

}
