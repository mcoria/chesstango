package net.chesstango.reports.search.evalcache;

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
        out.printf("EvaluationCacheReport  : %s%n", reportModel.searchGroupName);
        out.printf("Reads Node             : %8d%n", reportModel.readNodesTotal);
        out.printf("Reads NHits            : %8d (%2d%%)%n", reportModel.readNodeHitsTotal, reportModel.readNodeHitsPercentageTotal);
        out.printf("Reads Comparator       : %8d%n", reportModel.readComparatorsTotal);
        out.printf("Reads CHits            : %8d (%2d%%)%n", reportModel.readComparatorHitsTotal, reportModel.readComparatorHitsPercentageTotal);
        out.printf("Fill Avg               : %8d%%%n", reportModel.fillPercentageAvg);
        out.printf("%n");
        return this;
    }


    EvaluationCachePrinter printDetails() {
        out.printf("Evaluation Cache Statistics%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(6).setOut(out);

        printerTxtTable.setTitles("Move", "Reads Node", "Reads NHits", "Reads Comparator", "Reads CHits", "Fill");
        reportModel.moveDetails.forEach(moveDetail -> {

            printerTxtTable.addRow(moveDetail.move,
                    Long.toString(moveDetail.readNodes),
                    String.format("%d (%2d%%)", moveDetail.readNodeHits, moveDetail.readNodeHitsPercentage),
                    Long.toString(moveDetail.readComparators),
                    String.format("%d (%2d%%)", moveDetail.readComparatorHits, moveDetail.readComparatorHitsPercentage),
                    String.format("%2d%%", moveDetail.fillPercentage)
            );
        });

        printerTxtTable.setBottomRow("SUM",
                Long.toString(reportModel.readNodesTotal),
                String.format("%d (%2d%%)", reportModel.readNodeHitsTotal, reportModel.readNodeHitsPercentageTotal),
                Long.toString(reportModel.readComparatorsTotal),
                String.format("%d (%2d%%)", reportModel.readComparatorHitsTotal, reportModel.readComparatorHitsPercentageTotal),
                String.format("%2d%%", reportModel.fillPercentageAvg)
        );

        printerTxtTable.print();

        return this;
    }
}

