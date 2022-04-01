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
public class STOP implements UCIRequest {

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.STOP;
	}

	@Override
	public UCIResponse execute(Engine engine) {
		engine.do_stop();
		return null;
	}

}
