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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(8).setOut(out);

        printerTxtTable.setTitles("Move", "Reads", "Read NHits", "Read CHits", "Writes", "Updates", "OverWrites", "Fill");
        transpositionModel.transpositionModelDetail.forEach(row -> {
            printerTxtTable.addRow(
                    row.move,
                    Long.toString(row.reads),
                    String.format("%d (%2d%%)", row.readNodeHits, row.readNodeHitPercentage),
                    String.format("%d (%2d%%)", row.readComparatorHits, row.readComparatorHitPercentage),
                    Long.toString(row.writes),
                    String.format("%d (%2d%%)", row.updates, row.updatesPercentage),
                    String.format("%d (%2d%%)", row.overWrites, row.overWritePercentage),
                    String.format("%2d%%", row.maxMapFillPercentage)
            );
        });

        printerTxtTable.setBottomRow(
                "SUM",
                Long.toString(transpositionModel.readsTotal),
                String.format("%d (%2d%%)", transpositionModel.readNodeHitsTotal, transpositionModel.readNodeHitPercentageTotal),
                String.format("%d (%2d%%)", transpositionModel.readComparatorHitsTotal, transpositionModel.readComparatorHitPercentageTotal),
                Long.toString(transpositionModel.writesTotal),
                String.format("%d (%2d%%)", transpositionModel.updatesTotal, transpositionModel.updatesPercentageTotal),
                String.format("%d (%2d%%)", transpositionModel.overWritesTotal, transpositionModel.overWritesPercentageTotal),
                String.format("%2d%%", transpositionModel.maxMapFillPercentageAvg)
        );

        printerTxtTable.print();

        return this;
    }
}
