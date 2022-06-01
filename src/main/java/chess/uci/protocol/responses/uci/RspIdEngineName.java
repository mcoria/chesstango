package chess.uci.protocol.responses.uci;

import chess.uci.protocol.UCIResponseType;
import chess.uci.protocol.responses.RspAbstractSinlgeResponse;

/**
 * @author Mauricio Coria
 *
 */
public class RspIdEngineName extends RspAbstractSinlgeResponse {

	@Override
	public UCIResponseType getType() {
		return UCIResponseType.ID;
	}


	@Override
	public String encode() {
		return "id name Zonda Engine";
	}


}
