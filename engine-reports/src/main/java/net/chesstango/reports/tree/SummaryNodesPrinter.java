package net.chesstango.reports.tree;


import java.io.PrintStream;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class SummaryNodesPrinter {
    private final List<SummaryModel> reportRows;

    private final PrintStream out;

    private int maxRLevelVisited;

    private int maxQLevelVisited;

    public SummaryNodesPrinter(PrintStream out, List<SummaryModel> reportRows) {
        this.reportRows = reportRows;
        this.out = out;

        int maxRLevelVisited = 0;
        int maxQLevelVisited = 0;

        for (SummaryModel summaryModel : reportRows) {
            if (maxRLevelVisited < summaryModel.maxSearchRLevel) {
                maxRLevelVisited = summaryModel.maxSearchRLevel;
            }
            if (maxQLevelVisited < summaryModel.maxSearchQLevel) {
                maxQLevelVisited = summaryModel.maxSearchQLevel;
            }
        }

        this.maxRLevelVisited = maxRLevelVisited;
        this.maxQLevelVisited = maxQLevelVisited;
    }

    public SummaryNodesPrinter printNodesVisitedStatics() {
        out.println("\n Nodes visited per search level");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("___________"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("____________"));
        out.printf("______________"); // Nodes
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| Total Nodes ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.searchesName, row.searches);
            IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| %8d ", row.visitedRNodesCounters[depth]));
            IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| %9d ", row.visitedQNodesCounters[depth]));
            out.printf("| %11d ", row.visitedNodesTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("------------"));
        out.printf("--------------"); // Total Nodes
        out.printf("\n");

        return this;
    }

    public SummaryNodesPrinter printNodesVisitedStaticsByType() {
        out.println("\n Nodes visited per type");

        // Marco superior de la tabla
        out.printf(" _____________________________________________________________________________________________________________________________________");
        out.printf("\n");

        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES |       RNodes |       QNodes |  Total Nodes |  AVG RNodes |  AVG QNodes |   AVG Nodes ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.searchesName, row.searches);
            out.printf("| %12d ", row.visitedRNodesTotal);
            out.printf("| %12d ", row.visitedQNodesTotal);
            out.printf("| %12d ", row.visitedNodesTotal);
            out.printf("| %11d ", row.visitedRNodesAvg);
            out.printf("| %11d ", row.visitedQNodesAvg);
            out.printf("| %11d ", row.visitedNodesTotalAvg);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" -------------------------------------------------------------------------------------------------------------------------------------");
        out.printf("\n");

        return this;
    }

    public SummaryNodesPrinter printNodesVisitedStaticsAvg() {
        out.println("\n Nodes visited per search level AVG");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("___________"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("____________"));
        out.printf("______________"); // AVG Nodes/S
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| AVG Nodes/S ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.searchesName, row.searches);
            IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| %8d ", row.visitedRNodesCountersAvg[depth]));
            IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| %9d ", row.visitedQNodesCountersAvg[depth]));
            out.printf("| %11d ", row.visitedNodesTotalAvg);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("------------"));
        out.printf("--------------"); // AVG Nodes/S
        out.printf("\n");

        return this;
    }

}
