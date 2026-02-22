package net.chesstango.reports.search.evaluation;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.stream.IntStream;

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
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("EvaluationReport      : %s\n", reportModel.searchGroupName);
        out.printf("Evaluations           : %8d\n", reportModel.evaluationCounterTotal);
        out.printf("Cache Hits            : %8d\n", reportModel.evaluationsCacheHitCounterTotal);
        out.printf("Positions             : %8d\n", reportModel.evaluationPositionCounterTotal);
        out.printf("Values                : %8d\n", reportModel.evaluationValueCounterTotal);
        out.printf("Collisions            : %8d (%2d%%)\n", reportModel.evaluationPositionValueCollisionsCounterTotal, reportModel.evaluationCollisionPercentageTotal);
        out.print("\n");
        return this;
    }


    EvaluationPrinter printDetails() {
        out.printf("Evaluation Statistics%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(6).setOut(out);

        printerTxtTable.setTitles("Move", "Evaluations", "Cache Hits", "Positions", "Values", "Collisions");
        reportModel.moveDetails.forEach(moveDetail -> {

            printerTxtTable.addRow(moveDetail.move,
                    Long.toString(moveDetail.evaluationCounter),
                    Long.toString(moveDetail.evaluationsCacheHitCounter),
                    Long.toString(moveDetail.evaluationPositionCounter),
                    Long.toString(moveDetail.evaluationValueCounter),
                    String.format("%d (%2d%%)", moveDetail.evaluationPositionValueCollisionsCounter, moveDetail.evaluationPositionValueCollisionsPercentage));
        });

        printerTxtTable.setBottomRow("SUM",
                Long.toString(reportModel.evaluationCounterTotal),
                Long.toString(reportModel.evaluationsCacheHitCounterTotal),
                Long.toString(reportModel.evaluationPositionCounterTotal),
                Long.toString(reportModel.evaluationValueCounterTotal),
                String.format("%d (%2d%%)", reportModel.evaluationPositionValueCollisionsCounterTotal, reportModel.evaluationCollisionPercentageTotal));

        printerTxtTable.print();

        return this;
    }
}

