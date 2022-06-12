package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

public interface UCIOutputStream {
    void write(UCIMessage message);
}
