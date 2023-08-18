package net.chesstango.uci.protocol.stream.strings;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class StringActionSupplier implements Supplier<String> {

    private final Supplier<String> reader;
    private final Consumer<String> action;

    public StringActionSupplier(Supplier<String> reader, Consumer<String> action) {
        this.action = action;
        this.reader = reader;
    }

    @Override
    public String get() {
        String line = reader.get();
        action.accept(line);
        return line;
    }
}
