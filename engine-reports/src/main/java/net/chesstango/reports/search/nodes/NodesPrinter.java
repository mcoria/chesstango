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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(2 + reportModel.maxDepth + 1).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxDepth + 1).mapToObj(depth -> String.format("Depth %2d", depth)).forEach(tmp::add);
        tmp.add("Total");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        int[] visitedWidth = new int[reportModel.maxDepth + 1];
        int[] expectedWidth = new int[reportModel.maxDepth + 1];
        IntStream.range(0, reportModel.maxDepth + 1).forEach(depth -> {
            visitedWidth[depth] = String.format("%d", reportModel.visitedNodesCounters[depth]).length();
            expectedWidth[depth] = String.format("%d", reportModel.expectedNodesCounters[depth]).length();
        });

        int visitedTotalWidth = String.format("%d", reportModel.visitedNodesTotal).length();
        int expectedTotalWidth = String.format("%d", reportModel.expectedNodesTotal).length();

        reportModel.nodesModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(String.format("%s", moveDetail.move));
            IntStream.range(0, reportModel.maxDepth + 1).mapToObj(depth -> String.format("%" + visitedWidth[depth] + "d / %" + expectedWidth[depth] + "d", moveDetail.visitedNodesCounters[depth], moveDetail.expectedNodesCounters[depth])).forEach(tmpRow::add);
            tmpRow.add(String.format("%" + visitedTotalWidth + "d / %" + expectedTotalWidth + "d", moveDetail.visitedNodesCounter, moveDetail.expectedNodesCounter));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();
        tmp.add("SUM");
        IntStream.range(0, reportModel.maxDepth + 1).mapToObj(depth -> String.format("%d / %d", reportModel.visitedNodesCounters[depth], reportModel.expectedNodesCounters[depth])).forEach(tmp::add);
        tmp.add(String.format("%d / %d", reportModel.visitedNodesTotal, reportModel.expectedNodesTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();

        return this;
    }

}
