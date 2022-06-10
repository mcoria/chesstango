package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdUciNewGame implements UCIRequest {

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UCINEWGAME;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_newGame();
	}

	@Override
	public String toString() {
		return "ucinewgame";
	}
}
