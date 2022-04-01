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
public class WaitStartEngineCommand implements EngineState {
	
	private final Engine engine;

	public WaitStartEngineCommand(Engine engine) {
		this.engine = engine;
	}


	@Override
	public void do_start() {
		this.engine.selectState(new StartEngineCompleted(engine));
	}

}
