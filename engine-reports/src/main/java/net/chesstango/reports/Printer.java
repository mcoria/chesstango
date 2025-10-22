package net.chesstango.reports;

import java.io.PrintStream;

/**
 *
 * @author Mauricio Coria
 */
public interface Printer {

    void setOut(PrintStream out);

    void print();
}
