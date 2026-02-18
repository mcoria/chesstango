package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.search.nodes.NodesModel;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class SummaryCutoffPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    private List<NodesModel> reportRows;

    private int maxRLevelVisited;

    private int maxQLevelVisited;

    public SummaryCutoffPrinter setReportRows(List<NodesModel> reportRows) {
        this.reportRows = reportRows;

        int maxRLevelVisited = 0;
        int maxQLevelVisited = 0;

        for (NodesModel nodesModel : reportRows) {
            if (maxRLevelVisited < nodesModel.maxSearchRLevel) {
                maxRLevelVisited = nodesModel.maxSearchRLevel;
            }
            if (maxQLevelVisited < nodesModel.maxSearchQLevel) {
                maxQLevelVisited = nodesModel.maxSearchQLevel;
            }
        }

        this.maxRLevelVisited = maxRLevelVisited;
        this.maxQLevelVisited = maxQLevelVisited;

        return this;
    }

    @Override
    public SummaryCutoffPrinter print() {
        out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        out.printf(" __________________________________________________");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("____________"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("____________"));
        out.printf("____________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| ENGINE NAME                           | SEARCHES ");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| RLevel %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("|   Cutoff  ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("| %37s |%9d ", row.searchGroupName, row.searches);
            IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| %7d %% ", row.cutoffRPercentages[depth]));
            IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| %7d %% ", row.cutoffQPercentages[depth]));
            out.printf("| %7d %% ", row.cutoffPercentageTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" --------------------------------------------------");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("------------"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("------------"));
        out.printf("------------");
        out.printf("\n");

        return this;
    }
}
