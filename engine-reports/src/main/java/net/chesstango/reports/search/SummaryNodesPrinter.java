package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.nodes.NodesModel;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class SummaryNodesPrinter implements Printer {
    private List<NodesModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    private int maxSearchDepth;

    public SummaryNodesPrinter setReportRows(List<NodesModel> reportRows) {
        this.reportRows = reportRows;
        this.maxSearchDepth = 0;

        for (NodesModel nodesModel : reportRows) {
            if (maxSearchDepth < nodesModel.maxDepth) {
                maxSearchDepth = nodesModel.maxDepth;
            }
        }

        return this;
    }

    @Override
    public SummaryNodesPrinter print() {
        return this
                .printNodesVisitedStaticsByType()
                .printNodesVisitedStatics();
    }

    public SummaryNodesPrinter printNodesVisitedStaticsByType() {
        out.printf("%n Nodes visited per type %n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(10).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "RNodes", "INodes", "QNodes", "LNodes", "TNodes", "LoNodes", "ENode", "Nodes");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.rootNodeCounterTotal),
                    Long.toString(row.interiorNodeCounterTotal),
                    Long.toString(row.quiescenceNodeCounterTotal),
                    Long.toString(row.leafNodeCounterTotal),
                    Long.toString(row.terminalNodeCounterTotal),
                    Long.toString(row.loopNodeCounterTotal),
                    Long.toString(row.egtbCounterTotal),
                    Long.toString(row.nodeCounterTotal)
            );
        });
        printerTxtTable.print();

        return this;
    }


    public SummaryNodesPrinter printNodesVisitedStatics() {
        out.printf("%n Nodes visited per search level %n");

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
            IntStream.range(0, maxSearchDepth + 1).mapToObj(depth -> Long.toString(row.visitedRNodesCounters[depth])).forEach(tmpRow::add);
            tmpRow.add(Long.toString(row.visitedRNodesTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();

        return this;
    }

}
