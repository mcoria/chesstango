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
public class Stopped implements EngineState {

	private final Engine engine;
	
	public Stopped(Engine engine) {
		this.engine = engine;
	}


	@Override
	public void do_start() {
	}

	@Override
	public void do_newGame() {
		this.engine.selectState(new WaitPositionCommand(engine));		
	}


	@Override
	public void do_position_fen(String fen, List<String> moves) {
		this.engine.selectState(new WaitGoCommand(engine));		
	}


	@Override
	public void do_go(GO go) {
	}


	@Override
	public void do_stop() {
	}

}
