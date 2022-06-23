package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.Closeable;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIInputStream extends Supplier<UCIMessage>, Closeable {

}
