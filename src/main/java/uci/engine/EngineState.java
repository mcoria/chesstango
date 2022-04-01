/**
 * 
 */
package uci.engine;

import java.util.List;

import uci.protocol.imp.requests.GO;

/**
 * @author Mauricio Coria
 *
 */
public interface EngineState {

	void do_start();


	void do_newGame();


	void do_position_fen(String fen, List<String> moves);


	void do_go(GO go);


	void do_stop();

}
