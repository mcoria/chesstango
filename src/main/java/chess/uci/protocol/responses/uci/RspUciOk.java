/**
 * 
 */
package chess.uci.protocol.responses.uci;

import chess.uci.protocol.UCIResponseType;
import chess.uci.protocol.responses.RspAbstractSinlgeResponse;

/**
 * @author Mauricio Coria
 *
 */
public class RspUciOk extends RspAbstractSinlgeResponse {


	@Override
	public UCIResponseType getType() {
		return UCIResponseType.UCIOK;
	}
	

	@Override
	public String encode() {
		return "uciok";
	}

}
