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
public class GO implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.GO;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		engine.do_go(this);
		return null;
	}

}
