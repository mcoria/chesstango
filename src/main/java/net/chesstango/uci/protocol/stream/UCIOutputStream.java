package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;

import java.io.Closeable;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIOutputStream extends Consumer<UCIMessage>, Closeable {
}
