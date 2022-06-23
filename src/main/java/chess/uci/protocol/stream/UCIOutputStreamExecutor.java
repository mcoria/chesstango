package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIMessageExecutor;

import java.io.IOException;

/**
 * @author Mauricio Coria
 *
 */
public class UCIOutputStreamExecutor implements UCIOutputStream {

    private final UCIMessageExecutor executor;

    public UCIOutputStreamExecutor(UCIMessageExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void write(UCIMessage message) {
        message.execute(executor);
    }

    @Override
    public void close() throws IOException {
    }
}
