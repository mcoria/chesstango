/**
 * 
 */
package uci.protocol.imp.requests;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;
import uci.protocol.UCIResponse;
import uci.protocol.imp.responses.id.IdResponse;

/**
 * @author Mauricio Coria
 *
 */
public class UCI implements UCIRequest {
	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UCI;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		return new IdResponse();
	}

}
