package net.chesstango.uci.protocol;

import static net.chesstango.uci.protocol.UCIMessage.MessageType.Unknown;

/**
 * @author Mauricio Coria
 */
public class UCIMessageUnknown implements UCIMessage {

    private final String line;

    public UCIMessageUnknown(String line) {
        this.line = line;
    }

    @Override
    public MessageType getMessageType() {
        return Unknown;
    }

    @Override
    public String toString() {
        return line;
    }
}
