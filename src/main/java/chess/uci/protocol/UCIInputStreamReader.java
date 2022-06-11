package chess.uci.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UCIInputStreamReader implements UCIInputStream {
    private final UCIDecoder uciDecoder = new UCIDecoder();
    private final BufferedReader reader;

    public UCIInputStreamReader(InputStream inputStream) {
        this.reader =  new BufferedReader(new InputStreamReader(inputStream));
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
