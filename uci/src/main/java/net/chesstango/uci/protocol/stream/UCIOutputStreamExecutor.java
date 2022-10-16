package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.UCIMessageExecutor;

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
    public void accept(UCIMessage message) {
        message.execute(executor);
    }

    @Override
    public void close() throws IOException {
    }
}
