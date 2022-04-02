/**
 * 
 */
package uci.protocol.responses;

import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class RspReadyOk extends RspAbstractSinlgeResponse {


	@Override
	public UCIResponseType getType() {
		return UCIResponseType.READYOK;
	}


	@Override
	public String encode() {
		return "readyok";
	}

}
