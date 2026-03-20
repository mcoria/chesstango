package net.chesstango.reports.search.nodes.visited;

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
class NodesVisitedPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private NodesVisitedModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public NodesVisitedPrinter print() {
        out.print("Visited Nodes Statistics\n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(2 + reportModel.maxSelDepth + 1).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxSelDepth + 1).mapToObj(depth -> String.format("Depth %2d", depth)).forEach(tmp::add);
        tmp.add("Total");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        int[] visitedWidth = new int[reportModel.maxSelDepth + 1];
        int[] expectedWidth = new int[reportModel.maxSelDepth + 1];
        IntStream.range(0, reportModel.maxSelDepth + 1).forEach(depth -> {
            visitedWidth[depth] = String.format("%d", reportModel.visitedRNodesCounters[depth]).length();
            expectedWidth[depth] = String.format("%d", reportModel.expectedRNodesCounters[depth]).length();
        });

        int visitedTotalWidth = String.format("%d", reportModel.visitedRNodesTotal).length();
        int expectedTotalWidth = String.format("%d", reportModel.expectedRNodesTotal).length();

        reportModel.nodesModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(String.format("%s", moveDetail.move));
            IntStream.range(0, reportModel.maxSelDepth + 1).mapToObj(depth -> String.format("%" + visitedWidth[depth] + "d / %" + expectedWidth[depth] + "d", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth])).forEach(tmpRow::add);
            tmpRow.add(String.format("%" + visitedTotalWidth + "d / %" + expectedTotalWidth + "d", moveDetail.visitedRNodesCounter, moveDetail.expectedRNodesCounter));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();
        tmp.add("SUM");
        IntStream.range(0, reportModel.maxSelDepth + 1).mapToObj(depth -> String.format("%d / %d", reportModel.visitedRNodesCounters[depth], reportModel.expectedRNodesCounters[depth])).forEach(tmp::add);
        tmp.add(String.format("%d / %d", reportModel.visitedRNodesTotal, reportModel.expectedRNodesTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();

        return this;
    }

}
