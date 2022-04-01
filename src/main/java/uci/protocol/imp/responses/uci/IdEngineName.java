/**
 * 
 */
package uci.protocol.imp.responses.uci;

import uci.protocol.UCIResponseType;
import uci.protocol.imp.responses.AbstractSinlgeResponse;

/**
 * @author Mauricio Coria
 *
 */
public class IdEngineName extends AbstractSinlgeResponse {

	@Override
	public UCIResponseType getType() {
		return UCIResponseType.ID;
	}


	@Override
	public String encode() {
		return "id name Zonda Engine";
	}


}
