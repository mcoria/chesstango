package chess.uci.protocol.requests;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;

import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class CmdPositionStart implements UCIRequest {

	private final List<String> moves;
	

	public CmdPositionStart(List<String> moves) {
		this.moves = moves;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Request;
	}

	@Override
	public UCIRequestType getRequestType() {
		return UCIRequestType.POSITION;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_position_startpos(this);
	}

	
	public List<String> getMoves(){
		return moves;
	}

}
