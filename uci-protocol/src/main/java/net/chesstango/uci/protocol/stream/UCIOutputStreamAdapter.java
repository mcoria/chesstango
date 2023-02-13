package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
public class UCIOutputStreamAdapter implements UCIOutputStream {

    private final Consumer<String> out;

    public UCIOutputStreamAdapter(Consumer<String> out) {
        this.out = out;
    }

    @Override
    public void accept(UCIMessage message) {
        out.accept(message.toString());
    }

    @Override
    public void close() throws IOException {
        //out.close();
    }
}
