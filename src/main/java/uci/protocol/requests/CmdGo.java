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
public class CmdGo implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.GO;
	}


	@Override
	public void execute(Engine engine) {
		engine.do_go(this);
	}

}
