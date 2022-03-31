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
public class Unknown implements UCIRequest {

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see chessuci.protocol.UCIRequest#execute(chessuci.engine.Engine)
	 */
	@Override
	public UCIResponse execute(Engine engine) {
		return null;
	}

}
