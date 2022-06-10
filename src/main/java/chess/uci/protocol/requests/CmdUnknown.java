package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIRequest;

/**
 * @author Mauricio Coria
 *
 */
public class CmdUnknown implements UCIMessage {


	@Override
	public MessageType getMessageType() {
		return null;
	}


}
