package net.chesstango.uci.protocol.stream.strings;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class StringConsumer implements Consumer<String> {

    private final BufferedWriter out;

    public StringConsumer(Writer writer) {
        this.out = new BufferedWriter(writer);
    }

    @Override
    public void accept(String line) {
        try {
            if(line != null) {
                out.write(line);
                out.newLine();
                out.flush();
            } else {
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
