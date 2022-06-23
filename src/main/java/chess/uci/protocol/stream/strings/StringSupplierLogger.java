package chess.uci.protocol.stream.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 *
 */
public class StringSupplierLogger implements Supplier<String> {

    private final String logprefix;
    private final Supplier<String> reader;

    public StringSupplierLogger(String logprefix, Supplier<String> reader) {
        this.logprefix =  logprefix;
        this.reader =  reader;
    }

    @Override
    public String get() {
        String line = reader.get();
        System.out.println(logprefix + line);
        return line;
    }
}
