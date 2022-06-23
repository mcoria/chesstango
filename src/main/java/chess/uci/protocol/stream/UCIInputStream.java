package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.Closeable;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIInputStream extends Closeable {
    UCIMessage read();
}
