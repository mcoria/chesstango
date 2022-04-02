/**
 * 
 */
package uci.protocol.responses.uci;

import uci.protocol.UCIResponseType;
import uci.protocol.responses.RspAbstractSinlgeResponse;

/**
 * @author Mauricio Coria
 *
 */
public class RspIdEngineAuthor extends RspAbstractSinlgeResponse {

	@Override
	public UCIResponseType getType() {
		return UCIResponseType.ID;
	}


	@Override
	public String encode() {
		return "id author Mauricio Coria";
	}


}
