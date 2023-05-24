package net.chesstango.uci.protocol;

/**
 * @author Mauricio Coria
 */
public interface UCIResponse extends UCIMessage {

    enum UCIResponseType {
        ID, UCIOK, READYOK, INFO, BESTMOVE
    }

    UCIResponseType getResponseType();

    void execute(UCIGui executor);
}
