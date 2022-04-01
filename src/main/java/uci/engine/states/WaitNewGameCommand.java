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



	@Override
	public void do_position_fen(String fen, List<String> moves) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void do_go(GO go) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void do_stop() {

		
	}

}
