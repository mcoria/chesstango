package chess.uci.protocol;

import java.io.PrintStream;

public class UCIOutputStreamWriter implements UCIOutputStream{

    private final PrintStream out;

    public UCIOutputStreamWriter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void write(UCIMessage message) {
        out.println(message);
    }
}
