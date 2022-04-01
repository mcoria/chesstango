/**
 * 
 */
package uci.engine;

import uci.engine.states.WaitStartEngineCommand;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {
	private boolean keepProcessing = true;
	
	private EngineState engineState;
	

	public Engine() {
		selectState(new WaitStartEngineCommand(this));
	}

	public void do_start() {
		engineState.do_start();
	}
	
	public void do_quit() {
		keepProcessing = false;
	}

	public void do_ping() {
	}

	public void do_stop() {
	}

	public void do_go() {		
	}
	
	public boolean keepProcessing() {
		return keepProcessing;
	}

	public void selectState(EngineState newState) {
		engineState = newState;
	}
	
}
