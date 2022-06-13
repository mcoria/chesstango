package chess.uci.protocol.stream;

import chess.uci.protocol.UCIDecoder;
import chess.uci.protocol.UCIMessage;

import java.io.*;

public class UCIInputStreamReader implements UCIInputStream {
    private final UCIDecoder uciDecoder = new UCIDecoder();
    private final BufferedReader reader;

    public UCIInputStreamReader(Reader reader) {
        this.reader =  new BufferedReader(reader);
    }

    @Override
    public UCIMessage read() {
        try {
            String line = reader.readLine();
            return line == null ? null : uciDecoder.parseMessage(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
