/**
 * 
 */
package uci.protocol.imp.requests;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;

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
	public void execute(Engine engine) {
		engine.do_start();
	}

}
