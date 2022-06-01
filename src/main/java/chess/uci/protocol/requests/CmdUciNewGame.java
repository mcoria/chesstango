package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;
import chess.uci.protocol.UCIRequestType;

/**
 * @author Mauricio Coria
 *
 */
public class CmdUciNewGame implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UCINEWGAME;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_newGame();
	}

}
