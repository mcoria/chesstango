/**
 * 
 */
package uci.protocol.responses.uci;

import uci.protocol.responses.RspMultiline;

/**
 * @author Mauricio Coria
 *
 */
public class RspUci extends RspMultiline {

	public RspUci() {
		addResponse(new RspIdEngineName());
		addResponse(new RspIdEngineAuthor());
		addResponse(new RspUciOk());
	}

}
