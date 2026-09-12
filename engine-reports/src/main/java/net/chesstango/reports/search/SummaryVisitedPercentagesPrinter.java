package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.nodes.visited.VisitedModel;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class SummaryVisitedPercentagesPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    private List<VisitedModel> reportRows;

    private int maxDepth;

    public SummaryVisitedPercentagesPrinter setReportRows(List<VisitedModel> reportRows) {
        this.reportRows = reportRows;
        this.maxDepth = 0;

        for (VisitedModel nodesModel : reportRows) {
            if (maxDepth < nodesModel.maxDepth) {
                maxDepth = nodesModel.maxDepth;
            }
        }

        return this;
    }

    @Override
    public SummaryVisitedPercentagesPrinter print() {
        out.printf("%nVisited percentage per search level (lower is better)%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(3 + maxDepth + 1).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("ENGINE NAME");
        tmp.add("SEARCHES");
        IntStream.range(0, maxDepth + 1).mapToObj(depth -> String.format("Depth %2d", depth)).forEach(tmp::add);
        tmp.add("Visited %");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportRows.forEach(row -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(row.searchGroupName);
            tmpRow.add(Integer.toString(row.searches));
            IntStream.range(0, maxDepth + 1).mapToObj(depth -> String.format("%d %% ", row.visitedPercentages[depth])).forEach(tmpRow::add);
            tmpRow.add(Integer.toString(row.visitedPercentageTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();

        return this;
    }
}
