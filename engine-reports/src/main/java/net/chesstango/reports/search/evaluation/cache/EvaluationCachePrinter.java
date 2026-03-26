package net.chesstango.reports.search.evaluation.cache;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
class EvaluationCachePrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private EvaluationCacheModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public EvaluationCachePrinter print() {
        return printSummary()
                .printDetails();
    }

    EvaluationCachePrinter printSummary() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("EvaluationCacheReport : %s%n", reportModel.searchGroupName);
        out.printf("Evaluations           : %8d%n", reportModel.evaluationsCounterTotal);
        out.printf("Cache Hits            : %8d (%2d%%)%n%n", reportModel.evaluationsCacheHitsCounterTotal, reportModel.evaluationsCacheHitsPercentageTotal);
        out.printf("Read Cache            : %8d%n", reportModel.readFromCacheCounterTotal);
        out.printf("Read Cache Hits       : %8d (%2d%%)%n%n", reportModel.readFromCacheHitsCounterTotal, reportModel.readFromCacheHitsPercentageTotal);
        out.printf("Fill %% Avg           : %8d%%%n", reportModel.fillPercentageAvg);
        out.printf("%n");
        return this;
    }


    EvaluationCachePrinter printDetails() {
        out.printf("Evaluation Cache Statistics%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(6).setOut(out);

        printerTxtTable.setTitles("Move", "Evaluations", "Cache Hits", "Read Cache", "Read Cache Hits", "Fill %");
        reportModel.moveDetails.forEach(moveDetail -> {

            printerTxtTable.addRow(moveDetail.move,
                    Long.toString(moveDetail.evaluationsCounter),
                    String.format("%d (%2d%%)", moveDetail.evaluationsCacheHitsCounter, moveDetail.evaluationsCacheHitsPercentage),
                    Long.toString(moveDetail.readsFromCacheCounter),
                    String.format("%d (%2d%%)", moveDetail.readsFromCacheHitsCounter, moveDetail.readsFromCacheHitsPercentage),
                    String.format("%2d%%", moveDetail.fillPercentage)
            );
        });

        printerTxtTable.setBottomRow("SUM",
                Long.toString(reportModel.evaluationsCounterTotal),
                String.format("%d (%2d%%)", reportModel.evaluationsCacheHitsCounterTotal, reportModel.evaluationsCacheHitsPercentageTotal),
                Long.toString(reportModel.readFromCacheCounterTotal),
                String.format("%d (%2d%%)", reportModel.readFromCacheHitsCounterTotal, reportModel.readFromCacheHitsPercentageTotal),
                String.format("%2d%%", reportModel.fillPercentageAvg)
        );

        printerTxtTable.print();

        return this;
    }
}

