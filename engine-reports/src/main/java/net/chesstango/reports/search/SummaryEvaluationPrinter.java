package net.chesstango.reports.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.evaluation.EvaluationModel;

import java.io.PrintStream;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryEvaluationPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private List<EvaluationModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public SummaryEvaluationPrinter print() {
        out.println("\n Evaluation Statistics");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(7).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "Evaluations", "Cache Hits", "Positions", "Values", "Collisions");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.evaluationCounterTotal),
                    Long.toString(row.evaluationsCacheHitCounterTotal),
                    Long.toString(row.evaluationPositionCounterTotal),
                    Long.toString(row.evaluationValueCounterTotal),
                    String.format("%d (%2d%%)", row.evaluationPositionValueCollisionsCounterTotal, row.evaluationCollisionPercentageTotal));
        });
        printerTxtTable.print();

        return this;
    }
}
