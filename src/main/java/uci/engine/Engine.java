/**
 * 
 */
package uci.engine;

import java.util.List;

import uci.protocol.requests.GO;
import uci.protocol.responses.ReadyOk;
import uci.protocol.responses.uci.UciResponse;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {
	private boolean keepProcessing = true;
	
	private final UCIResponseChannel responseChannel;
	
	public Engine(UCIResponseChannel responseChannel) {
		this.responseChannel = responseChannel;
	}


	public void do_start() {
		responseChannel.send( new UciResponse() );
	}
	
	public void do_waitNewGame() {

	}
	
	public void do_position_startpos(List<String> moves) {

	}	
	
	public void do_position_fen(String fen, List<String> moves) {

	}
	
	public void do_go(GO go) {	

	}	
	
	public void do_quit() {
		keepProcessing = false;
	}

	public void do_ping() {
		responseChannel.send( new ReadyOk() );
	}

	public void do_stop() {
	}
	
	public boolean keepProcessing() {
		return keepProcessing;
	}


	public void do_setOptions() {
		// TODO Auto-generated method stub
		
	}

	
}
