package net.chesstango.reports.search.nodes;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
class NodesPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private NodesModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public NodesPrinter print() {
        out.print("Visited Nodes Statistics\n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(2 + reportModel.maxSearchRLevel).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxSearchRLevel).mapToObj(depth -> String.format("Depth %2d", depth)).forEach(tmp::add);
        tmp.add("Total");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.nodesModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(String.format("%s", moveDetail.move));
            IntStream.range(0, reportModel.maxSearchRLevel).mapToObj(depth -> String.format("%d / %d", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth])).forEach(tmpRow::add);
            tmpRow.add(String.format("%d / %d", moveDetail.visitedRNodesCounter, moveDetail.expectedRNodesCounter));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();
        tmp.add("SUM");
        IntStream.range(0, reportModel.maxSearchRLevel).mapToObj(depth -> String.format("%d / %d", reportModel.visitedRNodesCounters[depth], reportModel.expectedRNodesCounters[depth])).forEach(tmp::add);
        tmp.add(String.format("%d / %d", reportModel.visitedRNodesTotal, reportModel.expectedRNodesTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();

        return this;
    }

}
