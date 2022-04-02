/**
 * 
 */
package uci.protocol.responses.uci;

import uci.protocol.responses.RspMultilineImp;

/**
 * @author Mauricio Coria
 *
 */
public class RspUci extends RspMultilineImp {

	public RspUci() {
		addResponse(new RspIdEngineName());
		addResponse(new RspIdEngineAuthor());
		addResponse(new RspUciOk());
	}
}
