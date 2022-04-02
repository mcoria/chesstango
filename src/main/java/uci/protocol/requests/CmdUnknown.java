/**
 * 
 */
package uci.protocol.requests;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;

/**
 * @author Mauricio Coria
 *
 */
public class CmdUnknown implements UCIRequest {

	@Override
	public UCIRequestType getType() {
		return UCIRequestType.UNKNOWN;
	}


	@Override
	public void execute(Engine engine) {
	}

}
