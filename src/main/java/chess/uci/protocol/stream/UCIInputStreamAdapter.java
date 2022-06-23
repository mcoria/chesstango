package chess.uci.protocol.stream;

import chess.uci.protocol.UCIDecoder;
import chess.uci.protocol.UCIMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Mauricio Coria
 *
 */
public class UCIInputStreamAdapter implements UCIInputStream {
    private final UCIDecoder uciDecoder = new UCIDecoder();
    private final BufferedReader reader;

    public UCIInputStreamAdapter(Reader reader) {
        this.reader =  new BufferedReader(reader);
    }

    @Override
    public UCIMessage get() {
        try {
            String line = reader.readLine();
            System.out.println("<<" + line);
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
