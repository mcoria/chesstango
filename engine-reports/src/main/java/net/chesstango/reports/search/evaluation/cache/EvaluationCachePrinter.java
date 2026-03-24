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
        out.printf("EvaluationCacheReport : %s\n", reportModel.searchGroupName);
        out.printf("Evaluations           : %8d\n", reportModel.evaluationCounterTotal);
        out.printf("Cache Hits            : %8d\n", reportModel.evaluationsCacheHitsCounterTotal);
        out.printf("Read Cache            : %8d\n", reportModel.readFromCacheCounterTotal);
        out.printf("Read Cache Hits       : %8d\n", reportModel.readFromCacheHitsCounterTotal);
        out.print("\n");
        return this;
    }


    EvaluationCachePrinter printDetails() {
        out.printf("Evaluation Cache Statistics%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(5).setOut(out);

        printerTxtTable.setTitles("Move", "Evaluations", "Cache Hits", "Read Cache", "Read Cache Hits");
        reportModel.moveDetails.forEach(moveDetail -> {

            printerTxtTable.addRow(moveDetail.move,
                    Long.toString(moveDetail.evaluationCounter),
                    Long.toString(moveDetail.evaluationsCacheHitsCounter),
                    Long.toString(moveDetail.readFromCacheCounter),
                    Long.toString(moveDetail.readFromCacheHitsCounter));
        });

        printerTxtTable.setBottomRow("SUM",
                Long.toString(reportModel.evaluationCounterTotal),
                Long.toString(reportModel.evaluationsCacheHitsCounterTotal),
                Long.toString(reportModel.readFromCacheCounterTotal),
                Long.toString(reportModel.readFromCacheHitsCounterTotal));

        printerTxtTable.print();

        return this;
    }
}

