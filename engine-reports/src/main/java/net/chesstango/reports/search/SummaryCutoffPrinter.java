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
class SummaryCutoffPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    private List<NodesModel> reportRows;

    private int maxRLevelVisited;

    private int maxQLevelVisited;

    public SummaryCutoffPrinter setReportRows(List<NodesModel> reportRows) {
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
    public SummaryCutoffPrinter print() {
        out.printf("%n Cutoff per search level (higher is better)");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(3 + maxRLevelVisited + maxQLevelVisited).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("ENGINE NAME");
        tmp.add("SEARCHES");
        IntStream.range(0, maxRLevelVisited).mapToObj(depth -> String.format("RLevel %2d", depth + 1)).forEach(tmp::add);
        IntStream.range(0, maxQLevelVisited).mapToObj(depth -> String.format("QLevel %2d", depth + 1)).forEach(tmp::add);
        tmp.add("Cutoff");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportRows.forEach(row -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(row.searchGroupName);
            tmpRow.add(Integer.toString(row.searches));
            IntStream.range(0, maxRLevelVisited).mapToObj(depth -> String.format( "%d %% ", row.cutoffRPercentages[depth])).forEach(tmpRow::add);
            IntStream.range(0, maxQLevelVisited).mapToObj(depth -> String.format( "%d %% ", row.cutoffQPercentages[depth])).forEach(tmpRow::add);
            tmpRow.add(Integer.toString(row.cutoffPercentageTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();

        return this;
    }
}
