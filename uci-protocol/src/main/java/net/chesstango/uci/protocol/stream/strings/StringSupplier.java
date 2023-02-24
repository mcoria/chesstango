package net.chesstango.uci.protocol.stream.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class StringSupplier implements Supplier<String> {

    private final BufferedReader reader;

    public StringSupplier(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    @Override
    public String get() {
        try {
            String line = reader.readLine();
            return line;
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
