/**
 * 
 */
package uci.protocol;

import uci.engine.Engine;

/**
 * @author Mauricio Coria
 *
 */
public class GO implements UCIRequest {


	@Override
	public UCIRequestType getType() {
		return UCIRequestType.GO;
	}

	/* (non-Javadoc)
	 * @see uci.protocol.UCIRequest#execute(uci.engine.Engine)
	 */
	@Override
	public UCIResponse execute(Engine engine) {
		engine.do_go();
		return null;
	}

}
