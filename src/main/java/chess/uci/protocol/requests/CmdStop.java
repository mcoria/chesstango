package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;
import chess.uci.protocol.UCIRequestType;

/**
 * @author Mauricio Coria
 *
 */
public class CmdStop implements UCIRequest {

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.STOP;
	}

	@Override
	public void execute(Engine engine) {
		engine.do_stop();
	}

}
