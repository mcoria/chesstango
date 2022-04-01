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
public class WaitNewGameCommand implements EngineState{

	private final Engine engine;
	
	public WaitNewGameCommand(Engine engine) {
		this.engine = engine;
	}


	@Override
	public void do_start() {
		//Ignorar		
	}


	@Override
	public void do_newGame() {
		this.engine.selectState(new WaitPositionCommand(engine));
	}

}
