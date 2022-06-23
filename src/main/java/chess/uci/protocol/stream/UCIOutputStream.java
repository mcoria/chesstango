package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.Closeable;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIOutputStream extends Consumer<UCIMessage>, Closeable {
}
