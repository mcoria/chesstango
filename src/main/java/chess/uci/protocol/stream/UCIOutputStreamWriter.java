package chess.uci.protocol.stream;

import chess.uci.protocol.UCIMessage;

import java.io.*;

public class UCIOutputStreamWriter implements UCIOutputStream{

    private final BufferedWriter out;

    public UCIOutputStreamWriter(Writer writer) {
        this.out = new BufferedWriter(writer);
    }

    @Override
    public void write(UCIMessage message) {
        try {
            out.write(message.toString());
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
