package net.chesstango.reports.search.pv.iteration;

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
class PrincipalVariationIterationPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private PrincipalVariationIterationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public PrincipalVariationIterationPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        out.printf("PrincipalVariationIterationReport   : %s%n%n", reportModel.searchGroupName);
        out.printf("Searches                            : %10d%n", reportModel.searches);

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(reportModel.maxIteration + 1).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxIteration).mapToObj(it -> String.format("Iteration %2d", it + 1)).forEach(tmp::add);

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.modelDetails.forEach(detail -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(String.format("%s", detail.move));
            IntStream.range(0, reportModel.maxIteration).mapToObj(it -> it < detail.maxIteration ? Boolean.toString(detail.pvComplete[it]) : "-").forEach(tmpRow::add);

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();


        return this;
    }
}
