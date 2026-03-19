package net.chesstango.reports.search.iteration;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class IterationEvaluationPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private IterationEvaluationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public IterationEvaluationPrinter print() {
        out.print("Iteration Evaluation Statistics\n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(reportModel.maxIteration + 4).setOut(out);

        List<String> tmp = new LinkedList<>();
        tmp.add("Move");
        IntStream.range(0, reportModel.maxIteration).mapToObj(it -> String.format("Iteration %2d", it + 1)).forEach(tmp::add);
        tmp.add("Min");
        tmp.add("Max");
        tmp.add("Width");

        printerTxtTable.setTitles(tmp.toArray(new String[0]));

        reportModel.iterationModelDetails.forEach(moveDetail -> {
            List<String> tmpRow = new LinkedList<>();
            tmpRow.add(String.format("%s", moveDetail.move));
            IntStream.range(0, reportModel.maxIteration).mapToObj(it -> it < moveDetail.maxIteration ? Integer.toString(moveDetail.evaluations[it]) : "-").forEach(tmpRow::add);
            tmpRow.add(Integer.toString(moveDetail.minEvaluation));
            tmpRow.add(Integer.toString(moveDetail.maxEvaluation));
            tmpRow.add(Integer.toString(moveDetail.evaluationWidth));

            printerTxtTable.addRow(tmpRow.toArray(new String[0]));
        });

        printerTxtTable.print();


        return this;
    }
}
