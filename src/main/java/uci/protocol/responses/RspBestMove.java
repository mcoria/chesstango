/**
 * 
 */
package uci.protocol.responses;

import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class RspBestMove extends RspAbstractSinlgeResponse {
	
	private final String bestMove;
	
	public RspBestMove(String bestMove) {
		this.bestMove = bestMove;
	}

	@Override
	public UCIResponseType getType() {
		return UCIResponseType.BESTMOVE;
	}

	@Override
	public String encode() {
		return "bestmove " + bestMove;
	}

}
