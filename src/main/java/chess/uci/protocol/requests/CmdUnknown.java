package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;
import chess.uci.protocol.UCIRequestType;

/**
 * @author Mauricio Coria
 *
 */
public class CmdUnknown implements UCIRequest {

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UNKNOWN;
	}


	@Override
	public void execute(Engine engine) {
		throw new RuntimeException("Excecuting unknown command");
	}

}
