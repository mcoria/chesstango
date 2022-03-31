/**
 * 
 */
package uci.protocol.imp.responses.id;

import uci.protocol.imp.responses.MultilineImp;

/**
 * @author Mauricio Coria
 *
 */
public class IdResponse extends MultilineImp{


	public IdResponse() {
		addResponse(new EngineName());
		addResponse(new EngineAuthor());
	}
}
