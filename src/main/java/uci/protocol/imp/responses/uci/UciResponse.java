/**
 * 
 */
package uci.protocol.imp.responses.uci;

import uci.protocol.imp.responses.MultilineImp;

/**
 * @author Mauricio Coria
 *
 */
public class UciResponse extends MultilineImp{


	public UciResponse() {
		addResponse(new IdEngineName());
		addResponse(new IdEngineAuthor());
		addResponse(new UciOk());
	}
}
