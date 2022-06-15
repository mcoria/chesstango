package chess.uci.engine;

import chess.uci.protocol.stream.UCIOutputStream;

public interface Engine extends UCIOutputStream {

    void setResponseOutputStream(UCIOutputStream output);
}
