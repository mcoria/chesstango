/**
 * 
 */
package uci.protocol.requests;

import java.util.List;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;

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
