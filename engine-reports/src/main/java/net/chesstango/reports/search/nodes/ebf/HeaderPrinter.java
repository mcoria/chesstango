package net.chesstango.reports.search.nodes.ebf;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
class HeaderPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private EbfModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public HeaderPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("NodesVisitedReport    : %s%n%n", reportModel.searchGroupName);
        out.printf("Searches              : %10d%n", reportModel.searches);
        out.printf("%n");

        return this;
    }

}
