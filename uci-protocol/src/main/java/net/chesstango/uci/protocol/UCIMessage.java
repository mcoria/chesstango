package net.chesstango.uci.protocol;

public interface UCIMessage {
    enum MessageType {
        Request,
        Response,
        Unknown,
    }

    MessageType getMessageType();
}
