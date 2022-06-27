package net.chesstango.uci.protocol;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIResponse extends UCIMessage {

    enum UCIResponseType {
		ID, UCIOK, READYOK, BESTMOVE
	}

	UCIResponseType getResponseType();

}
