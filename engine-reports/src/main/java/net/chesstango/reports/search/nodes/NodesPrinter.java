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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(4 + reportModel.maxSearchRLevel + reportModel.maxSearchQLevel).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxSearchRLevel).mapToObj(depth -> String.format("RLevel %2d", depth + 1)).forEach(tmp::add);
        IntStream.range(0, reportModel.maxSearchQLevel).mapToObj(depth -> String.format("QLevel %2d", depth + 1)).forEach(tmp::add);
        tmp.add("RTotal");
        tmp.add("QTotal");
        tmp.add("Total");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.nodesModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();

            tmpRow.add(String.format("%s", moveDetail.move));

            IntStream.range(0, reportModel.maxSearchRLevel).mapToObj(depth -> String.format("%d / %d", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth])).forEach(tmpRow::add);
            IntStream.range(0, reportModel.maxSearchQLevel).mapToObj(depth -> String.format("%d / %d", moveDetail.visitedQNodesCounters[depth], moveDetail.expectedQNodesCounters[depth])).forEach(tmpRow::add);

            tmpRow.add(String.format("%d / %d", moveDetail.visitedRNodesCounter, moveDetail.expectedRNodesCounter));
            tmpRow.add(String.format("%d / %d", moveDetail.visitedQNodesCounter, moveDetail.expectedQNodesCounter));
            tmpRow.add(String.format("%d / %d", moveDetail.visitedNodesTotal, moveDetail.expectedNodesTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();
        tmp.add("SUM");
        IntStream.range(0, reportModel.maxSearchRLevel).mapToObj(depth -> String.format("%d / %d", reportModel.visitedRNodesCounters[depth], reportModel.expectedRNodesCounters[depth])).forEach(tmp::add);
        IntStream.range(0, reportModel.maxSearchQLevel).mapToObj(depth -> String.format("%d / %d", reportModel.visitedQNodesCounters[depth], reportModel.expectedQNodesCounters[depth])).forEach(tmp::add);
        tmp.add(String.format("%d / %d", reportModel.visitedRNodesTotal, reportModel.expectedRNodesTotal));
        tmp.add(String.format("%d / %d", reportModel.visitedQNodesTotal, reportModel.expectedQNodesTotal));
        tmp.add(String.format("%d / %d", reportModel.visitedNodesTotal, reportModel.expectedNodesTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();

        return this;
    }

}
