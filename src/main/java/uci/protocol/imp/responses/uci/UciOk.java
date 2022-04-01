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
public class UciOk extends AbstractSinlgeResponse {


	@Override
	public UCIResponseType getType() {
		return UCIResponseType.UCIOK;
	}
	

	@Override
	public String encode() {
		return "uciok";
	}

}
