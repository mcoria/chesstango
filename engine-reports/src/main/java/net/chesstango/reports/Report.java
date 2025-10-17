package net.chesstango.reports;

import java.io.PrintStream;

/**
 *
 * @author Mauricio Coria
 */
public interface Report {
    Report printReport(PrintStream out);
}
