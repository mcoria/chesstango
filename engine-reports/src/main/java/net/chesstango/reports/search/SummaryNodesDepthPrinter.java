package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.nodes.depth.NodesDepthModel;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class SummaryNodesDepthPrinter implements Printer {
    private List<NodesDepthModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    private int maxSearchDepth;

    public SummaryNodesDepthPrinter setReportRows(List<NodesDepthModel> reportRows) {
        this.reportRows = reportRows;
        this.maxSearchDepth = 0;

        for (NodesDepthModel nodesModel : reportRows) {
            if (maxSearchDepth < nodesModel.maxDepth) {
                maxSearchDepth = nodesModel.maxDepth;
            }
        }

        return this;
    }

    @Override
    public SummaryNodesDepthPrinter print() {
        out.printf("%n Nodes visited per depth%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(3 + maxSearchDepth + 1).setOut(out);

        List<String> tmp = new LinkedList<>();

        tmp.add("ENGINE NAME");
        tmp.add("SEARCHES");
        IntStream.range(0, maxSearchDepth + 1).mapToObj(depth -> String.format("Depth %2d", depth)).forEach(tmp::add);
        tmp.add("TOTAL NODES");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportRows.forEach(row -> {
            List<String> tmpRow = new LinkedList<>();

            tmpRow.add(row.searchGroupName);
            tmpRow.add(Integer.toString(row.searches));
            IntStream.range(0, maxSearchDepth + 1).mapToObj(depth -> Long.toString(row.visitedNodesCounters[depth])).forEach(tmpRow::add);
            tmpRow.add(Long.toString(row.visitedNodesTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();

        return this;
    }

}
