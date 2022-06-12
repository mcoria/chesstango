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
            return uciDecoder.parseMessage(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
