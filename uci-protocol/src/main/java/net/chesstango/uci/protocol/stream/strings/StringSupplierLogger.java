package net.chesstango.uci.protocol.stream.strings;

import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class StringSupplierLogger implements Supplier<String> {

    private final String logprefix;
    private final Supplier<String> reader;

    public StringSupplierLogger(String logprefix, Supplier<String> reader) {
        this.logprefix = logprefix;
        this.reader = reader;
    }

    @Override
    public String get() {
        String line = reader.get();
        if(line != null) {
            System.out.println(logprefix + line);
        }
        return line;
    }
}
