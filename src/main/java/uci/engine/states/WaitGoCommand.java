/**
 * 
 */
package uci.engine.states;

import java.util.List;

import uci.engine.Engine;
import uci.engine.EngineState;
import uci.protocol.imp.requests.GO;

/**
 * @author Mauricio Coria
 *
 */
public class WaitGoCommand implements EngineState {

	private final Engine engine;
	
	public WaitGoCommand(Engine engine) {
		this.engine = engine;
	}


	@Override
	public void do_start() {
	}


	@Override
	public void do_newGame() {
		
	}


	@Override
	public void do_position_fen(String fen, List<String> moves) {

	}


	@Override
	public void do_go(GO go) {
		// TODO Auto-generated method stub
		
	}

}
