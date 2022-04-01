/**
 * 
 */
package uci.engine;

import java.util.List;

import uci.engine.states.WaitStartEngineCommand;
import uci.protocol.imp.requests.GO;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {
	private boolean keepProcessing = true;
	
	private EngineState engineState;

	private static final String FEN_START = "";

	public Engine() {
		selectState(new WaitStartEngineCommand(this));
	}
	
	public void selectState(EngineState newState) {
		engineState = newState;
	}	

	public void do_start() {
		engineState.do_start();
	}
	
	public void do_waitNewGame() {
		engineState.do_newGame();
	}
	
	public void do_position_startpos(List<String> moves) {
		do_position_fen(FEN_START, moves);
	}	
	
	public void do_position_fen(String fen, List<String> moves) {
		engineState.do_position_fen(fen, moves);
	}
	
	public void do_go(GO go) {	
		engineState.do_go(go);
	}	
	
	public void do_quit() {
		keepProcessing = false;
	}

	public void do_ping() {
	}

	public void do_stop() {
	}
	
	public boolean keepProcessing() {
		return keepProcessing;
	}

	
}
