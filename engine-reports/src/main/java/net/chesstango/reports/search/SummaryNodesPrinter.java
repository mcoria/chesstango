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

    private int maxRLevelVisited;

    private int maxQLevelVisited;

    public SummaryNodesPrinter setReportRows(List<NodesModel> reportRows) {
        this.reportRows = reportRows;
        int maxRLevelVisited = 0;
        int maxQLevelVisited = 0;

        for (NodesModel nodesModel : reportRows) {
            if (maxRLevelVisited < nodesModel.maxSearchRLevel) {
                maxRLevelVisited = nodesModel.maxSearchRLevel;
            }
            if (maxQLevelVisited < nodesModel.maxSearchQLevel) {
                maxQLevelVisited = nodesModel.maxSearchQLevel;
            }
        }

        this.maxRLevelVisited = maxRLevelVisited;
        this.maxQLevelVisited = maxQLevelVisited;

        return this;
    }

    @Override
    public SummaryNodesPrinter print() {
        return this
                .printNodesVisitedStaticsByType()
                .printNodesVisitedStatics()
                .printNodesVisitedStaticsAvg();
    }

    public SummaryNodesPrinter printNodesVisitedStaticsByType() {
        out.println("\n Nodes visited per type");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(8).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "RNodes", "QNodes", "Total Nodes", "AVG RNodes", "AVG QNodes", "AVG Nodes");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.visitedRNodesTotal),
                    Long.toString(row.visitedQNodesTotal),
                    Long.toString(row.visitedNodesTotal),
                    Integer.toString(row.visitedRNodesAvg),
                    Integer.toString(row.visitedQNodesAvg),
                    Integer.toString(row.visitedNodesTotalAvg));
        });
        printerTxtTable.print();

        return this;
    }


    public SummaryNodesPrinter printNodesVisitedStatics() {
        out.println("\n Nodes visited per search level");

        /*
        // Marco superior de la tabla
        out.printf(" __________________________________________________");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("___________"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("____________"));
        out.printf("______________"); // Nodes
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| ENGINE NAME                           | SEARCHES ");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| Total Nodes ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("| %37s |%9d ", row.searchGroupName, row.searches);
            IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| %8d ", row.visitedRNodesCounters[depth]));
            IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| %9d ", row.visitedQNodesCounters[depth]));
            out.printf("| %11d ", row.visitedNodesTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" --------------------------------------------------");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("------------"));
        out.printf("--------------"); // Total Nodes
        out.printf("\n");

         */

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(3 + maxRLevelVisited + maxQLevelVisited).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("ENGINE NAME");
        tmp.add("SEARCHES");
        IntStream.range(0, maxRLevelVisited).mapToObj(depth -> String.format("RLevel %2d", depth + 1)).forEach(tmp::add);
        IntStream.range(0, maxQLevelVisited).mapToObj(depth -> String.format("QLevel %2d", depth + 1)).forEach(tmp::add);
        tmp.add("TOTAL NODES");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportRows.forEach(row -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(row.searchGroupName);
            tmpRow.add(Integer.toString(row.searches));
            IntStream.range(0, maxRLevelVisited).mapToObj(depth -> Long.toString(row.visitedRNodesCounters[depth])).forEach(tmpRow::add);
            IntStream.range(0, maxQLevelVisited).mapToObj(depth -> Long.toString(row.visitedQNodesCounters[depth])).forEach(tmpRow::add);
            tmpRow.add(Long.toString(row.visitedNodesTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();

        return this;
    }

    public SummaryNodesPrinter printNodesVisitedStaticsAvg() {
        out.println("\n Nodes visited per search level AVG");

        // Marco superior de la tabla
        out.printf(" __________________________________________________");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("___________"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("____________"));
        out.printf("______________"); // AVG Nodes/S
        out.printf("\n");


        // Nombre de las columnas
        out.printf("| ENGINE NAME                           | SEARCHES ");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| AVG Nodes/S ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("| %37s |%9d ", row.searchGroupName, row.searches);
            IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("| %8d ", row.visitedRNodesCountersAvg[depth]));
            IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("| %9d ", row.visitedQNodesCountersAvg[depth]));
            out.printf("| %11d ", row.visitedNodesTotalAvg);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" --------------------------------------------------");
        IntStream.range(0, maxRLevelVisited).forEach(depth -> out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited).forEach(depth -> out.printf("------------"));
        out.printf("--------------"); // AVG Nodes/S
        out.printf("\n");

        return this;
    }

}
