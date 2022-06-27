package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;

import java.io.Closeable;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIInputStream extends Supplier<UCIMessage>, Closeable {

}
