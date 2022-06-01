package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponseType;

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
