package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIEngine;
import net.chesstango.uci.protocol.UCIMessage;
import net.chesstango.uci.protocol.UCIRequest;

import java.io.IOException;

/**
 * @author Mauricio Coria
 *
 */
public class UCIOutputStreamEngineExecutor implements UCIOutputStream {

    private final UCIEngine executor;

    public UCIOutputStreamEngineExecutor(UCIEngine executor) {
        this.executor = executor;
    }

    @Override
    public void accept(UCIMessage message) {
        ((UCIRequest)message).execute(executor);
    }

    @Override
    public void close() throws IOException {
    }
}
