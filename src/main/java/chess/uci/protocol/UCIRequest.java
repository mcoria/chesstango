package chess.uci.protocol;

import chess.uci.engine.Engine;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIRequest extends UCIMessage {

	enum UCIRequestType {
		UCI, UNKNOWN, QUIT, ISREADY, STOP, GO, SETOPTION, UCINEWGAME, POSITION
	}


	UCIRequestType getType();


	void execute(Engine engine);
}
