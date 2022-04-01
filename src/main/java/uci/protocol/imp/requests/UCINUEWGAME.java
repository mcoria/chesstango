/**
 * 
 */
package uci.protocol.imp.requests;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;
import uci.protocol.UCIResponse;

/**
 * @author Mauricio Coria
 *
 */
public class UCINUEWGAME implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UCINEWGAME;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		engine.do_waitNewGame();
		return null;
	}

}
