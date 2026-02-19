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
        out.printf("TranspositionReport   : %s\n\n", transpositionModel.searchGroupName);
        out.printf("Searches              : %d\n", transpositionModel.searches);
        out.printf("Reads                 : %d\n", transpositionModel.readsTotal);
        out.printf("Read Hits             : %d (%2d%%)\n", transpositionModel.readHitsTotal, transpositionModel.readHitPercentageTotal);
        out.printf("Writes                : %d\n", transpositionModel.writesTotal);
        out.printf("OverWrites            : %d (%2d%%)\n", transpositionModel.overWritesTotal, transpositionModel.overWritePercentageTotal);
        out.print("\n");

        return this;
    }
}
