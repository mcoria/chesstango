package net.chesstango.reports.nodes;

import java.io.PrintStream;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class PrintVisitedNodes {

    private final PrintStream out;
    private final NodesReportModel reportModel;

    public PrintVisitedNodes(PrintStream out, NodesReportModel reportModel) {
        this.out = out;
        this.reportModel = reportModel;
    }

    public void printVisitedNodes() {
        out.printf("Visited Nodes Statistics\n");

        int longestId = 0;
        for (int i = 0; i < reportModel.moveDetails.size(); i++) {
            String epdId = reportModel.moveDetails.get(i).id;
            if (Objects.nonNull(epdId) && epdId.length() > longestId) {
                longestId = epdId.length();
            }
        }

        // Marco superior de la tabla
        out.printf(" ________");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("_____________________"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("_____________________"));
        out.printf("_____________________");
        out.printf("_____________________");
        out.printf("_____________________");
        out.printf("____________");
        if (longestId > 0) {
            out.printf(" %s", "_".repeat(longestId + 2));
        }
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|    RLevel %2d       ", depth + 1));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|    QLevel %2d       ", depth + 1));
        out.printf("|      RTotal        ");
        out.printf("|      QTotal        ");
        out.printf("|       Total        ");
        out.printf("| MovesExe  ");
        if (longestId > 0) {
            out.printf("| ID");
            out.printf(" ".repeat(longestId - 1));
        }
        out.printf("|\n");

        // Cuerpo
        for (NodesReportModel.SearchesReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);

            IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth]));
            IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| %7d / %8d ", moveDetail.visitedQNodesCounters[depth], moveDetail.expectedQNodesCounters[depth]));

            out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounter, moveDetail.expectedRNodesCounter);
            out.printf("| %7d / %8d ", moveDetail.visitedQNodesCounter, moveDetail.expectedQNodesCounter);
            out.printf("| %7d / %8d ", moveDetail.visitedNodesTotal, moveDetail.expectedNodesTotal);

            out.printf("|   %7d ", moveDetail.executedMoves);

            if (longestId > 0) {
                out.printf("| %" + longestId + "s ", moveDetail.id);
            }
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|--------------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|--------------------"));
        out.printf("|--------------------");
        out.printf("|--------------------");
        out.printf("|--------------------");
        out.printf("|-----------");
        if (longestId > 0) {
            out.printf("|");
            out.printf("-".repeat(longestId + 2));
        }
        out.printf("|\n");

        out.printf("| SUM    ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %7d / %8d ", reportModel.visitedRNodesCounters[depth], reportModel.expectedRNodesCounters[depth]));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| %7d / %8d ", reportModel.visitedQNodesCounters[depth], reportModel.expectedQNodesCounters[depth]));
        out.printf("| %7d / %8d ", reportModel.visitedRNodesTotal, reportModel.expectedRNodesTotal);
        out.printf("| %7d / %8d ", reportModel.visitedQNodesTotal, reportModel.expectedQNodesTotal);
        out.printf("| %7d / %8d ", reportModel.visitedNodesTotal, reportModel.expectedNodesTotal);
        out.printf("|   %7d ", reportModel.executedMovesTotal);
        if (longestId > 0) {
            out.printf("|");
            out.printf(" ".repeat(longestId + 2));
        }
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" ---------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("---------------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("---------------------"));
        out.printf("---------------------");
        out.printf("---------------------");
        out.printf("---------------------");
        out.printf("----------- ");
        if (longestId > 0) {
            out.printf("%s", "-".repeat(longestId + 2));
        }
        out.printf("\n\n");
    }
}
