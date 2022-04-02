/**
 * 
 */
package uci.protocol.responses.uci;

import uci.protocol.responses.MultilineImp;

/**
 * @author Mauricio Coria
 *
 */
public class UciResponse extends MultilineImp {

	public UciResponse() {
		addResponse(new IdEngineName());
		addResponse(new IdEngineAuthor());
		addResponse(new UciOk());
	}
}
