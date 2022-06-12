package chess.uci.protocol;

import chess.uci.ui.EngineClient;
import chess.uci.ui.EngineClientResponseListener;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIResponse extends UCIMessage {

    void execute(EngineClientResponseListener engineClient);

    enum UCIResponseType {
		ID, UCIOK, READYOK, BESTMOVE
	}

	UCIResponseType getResponseType();

}
