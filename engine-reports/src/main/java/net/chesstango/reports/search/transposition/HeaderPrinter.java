package net.chesstango.reports.search.transposition;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class HeaderPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private TranspositionModel transpositionModel;

    @Override
    public HeaderPrinter print() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("TranspositionReport: %s\n\n", transpositionModel.searchGroupName);
        out.printf("Searches              : %8d\n", transpositionModel.searches);
        out.printf("Hits                  : %8d\n", transpositionModel.hitsTotal);
        out.printf("Replaces              : %8d\n", transpositionModel.replacesTotal);
        out.print("\n");

        return this;
    }
}
