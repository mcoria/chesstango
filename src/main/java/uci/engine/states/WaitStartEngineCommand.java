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
public class WaitStartEngineCommand implements EngineState {
	
	private final Engine engine;

	public WaitStartEngineCommand(Engine engine) {
		this.engine = engine;
	}


	@Override
	public void do_start() {
		this.engine.selectState(new WaitNewGameCommand(engine));
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


	@Override
	public void do_stop() {
	}

}
