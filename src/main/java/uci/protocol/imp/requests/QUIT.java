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
public class QUIT implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.QUIT;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		return engine.quit();
	}

}
