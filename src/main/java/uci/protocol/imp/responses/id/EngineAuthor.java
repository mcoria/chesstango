/**
 * 
 */
package uci.protocol.imp.responses.id;

import uci.protocol.UCIResponseSingle;
import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class EngineAuthor implements UCIResponseSingle {

	@Override
	public UCIResponseType getType() {
		return UCIResponseType.ID;
	}


	@Override
	public String toString() {
		return "id author Mauricio Coria";
	}


}
