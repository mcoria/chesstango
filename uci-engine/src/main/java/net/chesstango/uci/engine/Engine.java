package net.chesstango.uci.engine;

import net.chesstango.uci.protocol.UCIRequest;
import net.chesstango.uci.protocol.UCIResponse;
import net.chesstango.uci.protocol.stream.UCIOutputStream;

/**
 * @author Mauricio Coria
 *
 */
public interface Engine extends UCIOutputStream {
    void open();
    void close();
    void setResponseOutputStream(UCIOutputStream output);
}
