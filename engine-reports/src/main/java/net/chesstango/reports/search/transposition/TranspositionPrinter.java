package net.chesstango.reports.search.transposition;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class TranspositionPrinter implements Printer {
    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Setter
    @Accessors(chain = true)
    private TranspositionModel transpositionModel;

    @Override
    public TranspositionPrinter print() {
        out.println("Transposition Statistics");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(5).setOut(out);

        printerTxtTable.setTitles("Move", "Reads", "Read Hits", "Writes", "OverWrites");
        transpositionModel.transpositionModelDetail.forEach(row -> {
            printerTxtTable.addRow(
                    row.move,
                    Long.toString(row.reads),
                    String.format("%d (%2d%%)", row.readHits, row.readHitPercentage),
                    Long.toString(row.writes),
                    String.format("%d (%2d%%)", row.overWrites, row.overWritePercentage));
        });

        printerTxtTable.setBottomRow(
                "SUM",
                Long.toString(transpositionModel.readsTotal),
                String.format("%d (%2d%%)", transpositionModel.readHitsTotal, transpositionModel.readHitPercentageTotal),
                Long.toString(transpositionModel.writesTotal),
                String.format("%d (%2d%%)", transpositionModel.overWritesTotal, transpositionModel.overWritePercentageTotal));

        printerTxtTable.print();

        return this;
    }
}
