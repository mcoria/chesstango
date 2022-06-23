package chess.uci.engine;

import chess.uci.protocol.stream.UCIOutputStream;

/**
 * @author Mauricio Coria
 *
 */
public interface Engine extends UCIOutputStream {

    void open();

    void close();

    void setResponseOutputStream(UCIOutputStream output);
}
