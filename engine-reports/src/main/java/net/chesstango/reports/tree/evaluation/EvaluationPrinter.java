package net.chesstango.reports.tree.evaluation;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
class EvaluationPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private EvaluationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public EvaluationPrinter print() {
        return printSummary()
                .printDetails();
    }

    EvaluationPrinter printSummary() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("EvaluationReport      : %s\n", reportModel.reportTitle);
        out.printf("Evaluations           : %8d\n", reportModel.evaluationCounterTotal);
        out.printf("Cache Hits            : %8d\n", reportModel.evaluationsCacheHitCounterTotal);
        out.printf("Positions             : %8d\n", reportModel.evaluationPositionCounterTotal);
        out.printf("Values                : %8d\n", reportModel.evaluationValueCounterTotal);
        out.printf("Collisions            : %8d (%2d%%)\n", reportModel.evaluationPositionValueCollisionsCounterTotal, reportModel.evaluationCollisionPercentageTotal);
        out.print("\n");
        return this;
    }


    EvaluationPrinter printDetails() {
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
        for (EvaluationModel.EvaluationReportModelDetail moveDetail : reportModel.moveDetails) {
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
        out.printf("---------------");
        ;
        out.printf("------------------- ");
        if (longestId > 0) {
            out.printf("%s", "-".repeat(longestId + 2));
        }
        out.printf("\n");

        //out.printf("Collisions = posibles colisiones (puede o no serlo) \n");

        out.printf("\n");

        return this;
    }
}

