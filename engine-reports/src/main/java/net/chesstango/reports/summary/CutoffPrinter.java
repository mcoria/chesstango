package net.chesstango.reports.summary;


import java.io.PrintStream;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class CutoffPrinter {
    private final List<SearchesSummaryModel> reportRows;
    private final PrintStream out;

    private int maxRLevelVisited;

    private int maxQLevelVisited;

    CutoffPrinter(PrintStream out, List<SearchesSummaryModel> reportRows) {
        this.reportRows = reportRows;
        this.out = out;

        int maxRLevelVisited = 0;
        int maxQLevelVisited = 0;

        for (SearchesSummaryModel searchesSummaryModel : reportRows) {
            if (maxRLevelVisited < searchesSummaryModel.maxSearchRLevel) {
                maxRLevelVisited = searchesSummaryModel.maxSearchRLevel;
            }
            if (maxQLevelVisited < searchesSummaryModel.maxSearchQLevel) {
                maxQLevelVisited = searchesSummaryModel.maxSearchQLevel;
            }
        }

        this.maxRLevelVisited = maxRLevelVisited;
        this.maxQLevelVisited = maxQLevelVisited;
    }

    public void printCutoffStatics() {
        out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("____________"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("____________"));
        out.printf("____________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| RLevel %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("|   Cutoff  ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| %7d %% ", row.cutoffRPercentages[depth]));
            IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| %7d %% ", row.cutoffQPercentages[depth]));
            out.printf("| %7d %% ", row.cutoffPercentage);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("------------"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("------------"));
        out.printf("------------");
        out.printf("\n");
    }
}
