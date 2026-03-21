package net.chesstango.reports.search.nodes.ebf;

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
class EbfPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private EbfModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public EbfPrinter print() {
        out.print("Effective Branching factor (EBF)\n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(1 + reportModel.maxIteration).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxIteration).mapToObj(depth -> String.format("Iteration %2d", depth + 1)).forEach(tmp::add);
        printerTxtTable.setTitles(tmp.toArray(new String[0]));


        reportModel.nodesModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(String.format("%s", moveDetail.move));
            IntStream.range(0, 2).mapToObj(depth -> "").forEach(tmpRow::add);
            IntStream.range(2, reportModel.maxIteration).mapToObj(depth -> String.format("%2.2f", moveDetail.ebf[depth])).forEach(tmpRow::add);

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        //printerTxtTable.setBottomRow(tmp.toArray(new String[0]));

        printerTxtTable.print();

        return this;
    }

}
