package chess.uci.protocol;

import chess.uci.engine.Engine;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIRequest extends UCIMessage {

	enum UCIRequestType {
		UCI, QUIT, ISREADY, STOP, GO, SETOPTION, UCINEWGAME, POSITION
	}


	UCIRequestType getRequestType();


	void execute(Engine engine);
}
