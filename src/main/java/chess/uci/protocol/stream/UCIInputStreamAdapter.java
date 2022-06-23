package chess.uci.protocol.stream;

import chess.uci.protocol.UCIDecoder;
import chess.uci.protocol.UCIMessage;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class UCIInputStreamAdapter implements UCIInputStream {
    private final UCIDecoder uciDecoder = new UCIDecoder();
    private final Supplier<String> reader;

    public UCIInputStreamAdapter(Supplier<String> reader) {
        this.reader = reader;
    }

    @Override
    public UCIMessage get() {
        String line = reader.get();
        System.out.println("<<" + line);
        return line == null ? null : uciDecoder.parseMessage(line);
    }

    @Override
    public void close() throws IOException {
        //reader.close();
    }
}
