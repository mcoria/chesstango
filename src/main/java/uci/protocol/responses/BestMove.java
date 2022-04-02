/**
 * 
 */
package uci.protocol.responses;

import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class BestMove extends AbstractSinlgeResponse {
	
	private final String bestMove;
	
	public BestMove(String bestMove) {
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
