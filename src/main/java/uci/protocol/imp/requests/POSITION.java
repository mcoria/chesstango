/**
 * 
 */
package uci.protocol.imp.requests;

import java.util.List;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;
import uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class POSITION implements UCIRequest {

	private final List<String> moves;
	
	private final boolean isFen;
	

	public POSITION(boolean isFen, List<String> moves) {
		this.isFen = isFen;
		this.moves = moves;
	}

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.POSITION;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		engine.do_position_startpos(moves);
		return null;
	}


	public boolean isFen() {
		return isFen;
	}
	
	public List<String> getMoves(){
		return moves;
	}

}
