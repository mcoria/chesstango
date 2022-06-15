package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.Closeable;

public interface UCIOutputStream extends Closeable {
    void write(UCIMessage message);
}
