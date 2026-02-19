package net.chesstango.reports;

import java.io.PrintStream;

/**
 *
 * @author Mauricio Coria
 */
public interface Printer {

    Printer setOut(PrintStream out);

    Printer print();
}
