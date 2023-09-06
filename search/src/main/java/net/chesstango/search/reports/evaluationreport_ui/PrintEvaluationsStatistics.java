package net.chesstango.search.reports.evaluationreport_ui;

import net.chesstango.search.reports.EvaluationReportModel;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PrintEvaluationsStatistics {
    private final PrintStream out;
    private final EvaluationReportModel reportModel;

    public PrintEvaluationsStatistics(PrintStream out, EvaluationReportModel reportModel) {
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
        for (EvaluationReportModel.EvolutionReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            out.printf("| %11d ", moveDetail.evaluatedPositionsCounter);
            out.printf("| %12d ", moveDetail.evaluatedUniquePositionsCounter);
            out.printf("| %12d ", moveDetail.evaluatedUniquePositionsValuesCounter);
            out.printf("| %12d ", moveDetail.evaluatedUniquePositionsValuesCollisionsCounter);
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
        out.printf("| %11d ", reportModel.evaluatedPositionsCounterTotal);
        out.printf("| %12d ", reportModel.evaluatedUniquePositionsCounterTotal);
        out.printf("| %12d ", reportModel.evaluatedUniquePositionsValuesCounterTotal);
        out.printf("| %12d ", reportModel.evaluatedUniquePositionsValuesCollisionsCounterTotal);
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
