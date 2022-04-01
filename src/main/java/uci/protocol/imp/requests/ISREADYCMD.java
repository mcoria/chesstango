/**
 * 
 */
package uci.protocol.imp.requests;

import uci.engine.Engine;
import uci.protocol.UCIRequest;
import uci.protocol.UCIRequestType;
import uci.protocol.UCIResponse;
import uci.protocol.imp.responses.ReadyOk;

/**
 * @author Mauricio Coria
 *
 */
public class ISREADYCMD implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.ISREADY;
	}


	@Override
	public UCIResponse execute(Engine engine) {
		engine.do_ping();
		return new ReadyOk();
	}

}
