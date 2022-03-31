/**
 * 
 */
package uci.protocol.imp.responses.uci;

import uci.protocol.UCIResponseSingle;
import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class IdEngineName implements UCIResponseSingle {

	@Override
	public UCIResponseType getType() {
		return UCIResponseType.ID;
	}


	@Override
	public String toString() {
		return "id name Zonda Engine";
	}


}
