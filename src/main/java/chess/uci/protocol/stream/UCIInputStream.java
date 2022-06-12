package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

public interface UCIInputStream {
    UCIMessage read();
}
