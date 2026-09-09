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
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("TranspositionReport   : %s%n%n", transpositionModel.searchGroupName);
        out.printf("Searches              : %10d%n", transpositionModel.searches);
        out.printf("Reads Node            : %10d%n", transpositionModel.readsNodeTotal);
        out.printf("Reads NHits           : %10d (%2d%%)%n", transpositionModel.readNodeHitsTotal, transpositionModel.readNodeHitPercentageTotal);
        out.printf("Writes                : %10d%n", transpositionModel.writesTotal);
        out.printf("Updates               : %10d (%2d%%)%n", transpositionModel.updatesTotal, transpositionModel.updatesPercentageTotal);
        out.printf("OverWrites            : %10d (%2d%%)%n", transpositionModel.overWritesTotal, transpositionModel.overWritesPercentageTotal);

        out.printf("Reads Comparator      : %10d%n", transpositionModel.readComparatorTotal);
        out.printf("Reads Comparator Hits  : %10d (%2d%%)%n", transpositionModel.readComparatorHitsTotal , transpositionModel.readComparatorHitPercentageTotal);

        out.printf("Fill Avg              : %10d%%%n", transpositionModel.mapFillPercentageAvg);
        out.printf("%n");

        return this;
    }
}
