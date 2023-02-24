package net.chesstango.uci.protocol.stream;

import net.chesstango.uci.protocol.UCIMessage;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class UCIOutputStreamToStringAdapter implements UCIOutputStream {

    private final Consumer<String> out;

    public UCIOutputStreamToStringAdapter(Consumer<String> out) {
        this.out = out;
    }

    @Override
    public void accept(UCIMessage message) {
        out.accept(message.toString());
    }

}
