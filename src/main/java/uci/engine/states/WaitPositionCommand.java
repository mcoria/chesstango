/**
 * 
 */
package uci.engine.states;

import uci.engine.Engine;
import uci.engine.EngineState;

/**
 * @author Mauricio Coria
 *
 */
public class WaitPositionCommand implements EngineState {

	private final Engine engine;
	
	public WaitPositionCommand(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void do_start() {

	}

	@Override
	public void do_newGame() {

	}

}
