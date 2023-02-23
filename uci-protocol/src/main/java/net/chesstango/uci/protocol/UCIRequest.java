package net.chesstango.uci.protocol;

/**
 * @author Mauricio Coria
 */
public interface UCIRequest extends UCIMessage {

    enum UCIRequestType {
        UCI, QUIT, ISREADY, STOP, GO, SETOPTION, UCINEWGAME, POSITION
    }

    UCIRequestType getRequestType();

    void execute(UCIEngine executor);
}
