/**
 * 
 */
package chess.uci.protocol.requests;

import java.util.List;

import chess.uci.engine.Engine;
import chess.uci.protocol.UCIRequest;
import chess.uci.protocol.UCIRequestType;

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
	public UCIRequestType getType() {
		return UCIRequestType.POSITION;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_position_startpos(moves);
	}

	
	public List<String> getMoves(){
		return moves;
	}

}
