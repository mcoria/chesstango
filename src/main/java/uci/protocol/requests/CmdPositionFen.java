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
public class CmdPositionFen implements UCIRequest {

	private final List<String> moves;
	
	private final String fen;
	

	public CmdPositionFen(String fen, List<String> moves) {
		this.fen = fen;
		this.moves = moves;
	}

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.POSITION;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_position_fen(fen, moves);
	}


	public String fen() {
		return fen;
	}
	
	public List<String> getMoves(){
		return moves;
	}

}
