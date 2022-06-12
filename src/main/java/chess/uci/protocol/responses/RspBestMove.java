package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponse;
import chess.uci.ui.EngineClient;
import chess.uci.ui.EngineClientResponseListener;

/**
 * @author Mauricio Coria
 *
 */
public class RspBestMove implements UCIResponse {
	
	private final String bestMove;
	
	public RspBestMove(String bestMove) {
		this.bestMove = bestMove;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Response;
	}

	@Override
	public void execute(EngineClientResponseListener engineClient) {
		engineClient.receive_bestMove(this);
	}

	@Override
	public UCIResponseType getResponseType() {
		return UCIResponseType.BESTMOVE;
	}

	@Override
	public String toString() {
		return "bestmove " + bestMove;
	}

}
