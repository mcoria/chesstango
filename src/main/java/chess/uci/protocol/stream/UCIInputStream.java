package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.Closeable;

public interface UCIInputStream extends Closeable {
    UCIMessage read();
}
