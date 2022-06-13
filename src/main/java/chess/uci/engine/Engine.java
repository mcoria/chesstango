package chess.uci.engine;

import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.stream.UCIInputStream;
import chess.uci.protocol.stream.UCIOutputStream;

public interface Engine extends UCIMessageExecutor {

    void setInputStream(UCIInputStream input);

    void setOutputStream(UCIOutputStream output);

    void main();
}
