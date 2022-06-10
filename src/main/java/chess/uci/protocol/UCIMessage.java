package chess.uci.protocol;

public interface UCIMessage {
    enum MessageType{
        Request,
        Response
    }

    MessageType getMessageType();
}
