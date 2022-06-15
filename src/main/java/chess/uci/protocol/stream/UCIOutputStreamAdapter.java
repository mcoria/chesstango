package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class UCIOutputStreamAdapter implements UCIOutputStream {

    private final BufferedWriter out;

    public UCIOutputStreamAdapter(Writer writer) {
        this.out = new BufferedWriter(writer);
    }

    @Override
    public void write(UCIMessage message) {
        try {
            out.write(message.toString());
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
