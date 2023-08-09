package net.chesstango.uci.protocol;

/**
 * @author Mauricio Coria
 */
public interface UCIMessage {
    enum MessageType {
        Request,
        Response,
        Unknown,
    }

    MessageType getMessageType();
}
