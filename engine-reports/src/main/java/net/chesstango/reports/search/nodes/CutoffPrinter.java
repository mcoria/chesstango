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
class CutoffPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private NodesModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public CutoffPrinter print() {
        out.printf("%nCutoff per search level (higher is better)%n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(2 + reportModel.maxSearchDepth + 1).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxSearchDepth + 1).mapToObj(depth -> String.format("Depth %2d", depth)).forEach(tmp::add);
        tmp.add("Cutoff");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.nodesModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();

            tmpRow.add(String.format("%s", moveDetail.move));
            IntStream.range(0, reportModel.maxSearchDepth + 1).mapToObj(depth -> String.format("%d %%", moveDetail.cutoffRPercentages[depth])).forEach(tmpRow::add);
            tmpRow.add(String.format("%d %%", moveDetail.cutoffPercentageTotal));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        tmp = new LinkedList<>();

        tmp.add("AVG");
        IntStream.range(0, reportModel.maxSearchDepth + 1).mapToObj(depth -> String.format("%d %%", reportModel.cutoffRPercentages[depth])).forEach(tmp::add);
        tmp.add(String.format("%d %%", reportModel.cutoffPercentageTotal));

        printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();

        return this;
    }

}
