package net.chesstango.reports.evaluation;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class EvaluationStatisticsReport {
    private final PrintStream out;
    private final EvaluationReportModel reportModel;

    public EvaluationStatisticsReport(PrintStream out, EvaluationReportModel reportModel) {
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
        //out.printf("____________");
        out.printf("______________");
        out.printf("______________");
        out.printf("_______________");
        out.printf("_______________");
        out.printf("____________________");
        if (longestId > 0) {
            out.printf(" %s", "_".repeat(longestId + 2));
        }
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        //out.printf("| Collisions ");
        out.printf("| Evaluations ");
        out.printf("| Cache Hits  ");
        out.printf("| Positions    ");
        out.printf("| Values       ");
        out.printf("| PosVal Collisions ");
        if (longestId > 0) {
            out.printf("| ID");
            out.printf(" ".repeat(longestId - 1));
        }
        out.printf("|\n");

        // Cuerpo
        for (EvaluationReportModel.EvaluationReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            //out.printf("| %10d ", moveDetail.possibleCollisionsCounter);
            out.printf("| %11d ", moveDetail.evaluationCounter);
            out.printf("| %11d ", moveDetail.evaluationsCacheHitCounter);
            out.printf("| %12d ", moveDetail.evaluationPositionCounter);
            out.printf("| %12d ", moveDetail.evaluationValueCounter);
            out.printf("| %11d (%2d%%) ", moveDetail.evaluationPositionValueCollisionsCounter, moveDetail.evaluationPositionValueCollisionsPercentage);
            if (longestId > 0) {
                out.printf("| %" + longestId + "s ", moveDetail.id);
            }
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
       // out.printf("|------------");
        out.printf("|-------------");
        out.printf("|-------------");
        out.printf("|--------------");
        out.printf("|--------------");
        out.printf("|-------------------");
        if (longestId > 0) {
            out.printf("|");
            out.printf("-".repeat(longestId + 2));
        }
        out.printf("|\n");


        out.printf("| SUM    ");
        //out.printf("|            ");
        out.printf("| %11d ", reportModel.evaluationCounterTotal);
        out.printf("| %11d ", reportModel.evaluationsCacheHitCounterTotal);
        out.printf("| %12d ", reportModel.evaluationPositionCounterTotal);
        out.printf("| %12d ", reportModel.evaluationValueCounterTotal);
        out.printf("| %11d (%2d%%) ", reportModel.evaluationPositionValueCollisionsCounterTotal, reportModel.evaluationCollisionPercentageTotal);
        if (longestId > 0) {
            out.printf("|");
            out.printf(" ".repeat(longestId + 2));
        }
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" ---------");
        //out.printf("--------------");
        out.printf("--------------");
        out.printf("--------------");
        out.printf("---------------");
        out.printf("---------------");;
        out.printf("------------------- ");
        if (longestId > 0) {
            out.printf("%s", "-".repeat(longestId + 2));
        }
        out.printf("\n");

        //out.printf("Collisions = posibles colisiones (puede o no serlo) \n");

        out.printf("\n");


    }
}

