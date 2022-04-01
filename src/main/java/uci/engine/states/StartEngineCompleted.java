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
public class StartEngineCompleted implements EngineState{

	private final Engine engine;
	
	public StartEngineCompleted(Engine engine) {
		this.engine = engine;
	}


	@Override
	public void do_start() {
		//Ignorar		
	}

}
