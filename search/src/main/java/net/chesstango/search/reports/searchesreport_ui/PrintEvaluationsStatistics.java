package net.chesstango.search.reports.searchesreport_ui;

import net.chesstango.search.reports.SearchesReportModel;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PrintEvaluationsStatistics {
    private final PrintStream out;
    private final SearchesReportModel reportModel;

    public PrintEvaluationsStatistics(PrintStream out, SearchesReportModel reportModel) {
        this.out = out;
        this.reportModel = reportModel;
    }

    public void printEvaluationsStatistics() {
        out.printf("Evaluation Statistics\n");

        int longestId = 0;
        for (int i = 0; i < reportModel.moveDetails.size(); i++) {
            String epdId = reportModel.moveDetails.get(i).id;
            if (Objects.nonNull(epdId) && epdId.length() > longestId) {
                longestId = epdId.length();
            }
        }

        // Marco superior de la tabla
        out.printf(" ________");
        out.printf("______________");
        out.printf("_______________");
        out.printf("_______________");
        out.printf("_______________");
        out.printf("______________");
        if (longestId > 0) {
            out.printf(" %s", "_".repeat(longestId + 2));
        }
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        out.printf("| Evaluations ");
        out.printf("| GEvaluations ");
        out.printf("| UEvaluations ");
        out.printf("| CEvaluations ");
        out.printf("| PCollisions ");
        if (longestId > 0) {
            out.printf("| ID");
            out.printf(" ".repeat(longestId - 1));
        }
        out.printf("|\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            out.printf("| %11d ", moveDetail.evaluatedGamesCounter);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounter);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounterUnique);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounterCollisions);
            out.printf("| %9d %% ", moveDetail.evaluationCollisionPercentage);
            if (longestId > 0) {
                out.printf("| %" + longestId + "s ", moveDetail.id);
            }
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
        out.printf("|-------------");
        out.printf("|--------------");
        out.printf("|--------------");
        out.printf("|--------------");
        out.printf("|-------------");
        if (longestId > 0) {
            out.printf("|");
            out.printf("-".repeat(longestId + 2));
        }
        out.printf("|\n");


        out.printf("| SUM    ");
        out.printf("| %11d ", reportModel.evaluatedGamesCounterTotal);
        out.printf("| %12d ", reportModel.distinctEvaluatedGamesCounterTotal);
        out.printf("| %12d ", reportModel.distinctEvaluatedGamesCounterUniqueTotal);
        out.printf("| %12d ", reportModel.distinctEvaluatedGamesCounterCollisionsTotal);
        out.printf("| %9d %% ", reportModel.evaluationCollisionPercentageTotal);
        if (longestId > 0) {
            out.printf("|");
            out.printf(" ".repeat(longestId + 2));
        }
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" ---------");
        out.printf("--------------");
        out.printf("---------------");
        out.printf("---------------");
        out.printf("---------------");
        out.printf("------------- ");
        if (longestId > 0) {
            out.printf("%s", "-".repeat(longestId + 2));
        }
        out.printf("\n\n");
    }
}
