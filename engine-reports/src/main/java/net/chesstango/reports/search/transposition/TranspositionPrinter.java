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

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(9).setOut(out);

        printerTxtTable.setTitles("Move", "Read Nodes", "Read NHits", "Writes", "Updates", "OverWrites", "Reads Comparator", "Reads CHits", "Fill");
        transpositionModel.transpositionModelDetail.forEach(row -> {
            printerTxtTable.addRow(
                    row.move,
                    Long.toString(row.readNodes),
                    String.format("%d (%2d%%)", row.readNodeHits, row.readNodeHitPercentage),
                    Long.toString(row.writes),
                    String.format("%d (%2d%%)", row.updates, row.updatesPercentage),
                    String.format("%d (%2d%%)", row.overWrites, row.overWritePercentage),
                    Long.toString(row.readComparators),
                    String.format("%d (%2d%%)", row.readComparatorHits, row.readComparatorHitPercentage),
                    String.format("%2d%%", row.mapFillPercentage)
            );
        });

        printerTxtTable.setBottomRow(
                "SUM",
                Long.toString(transpositionModel.readsNodeTotal),
                String.format("%d (%2d%%)", transpositionModel.readNodeHitsTotal, transpositionModel.readNodeHitPercentageTotal),
                Long.toString(transpositionModel.writesTotal),
                String.format("%d (%2d%%)", transpositionModel.updatesTotal, transpositionModel.updatesPercentageTotal),
                String.format("%d (%2d%%)", transpositionModel.overWritesTotal, transpositionModel.overWritesPercentageTotal),
                Long.toString(transpositionModel.readComparatorTotal),
                String.format("%d (%2d%%)", transpositionModel.readComparatorHitsTotal, transpositionModel.readComparatorHitPercentageTotal),
                String.format("%2d%%", transpositionModel.mapFillPercentageAvg)
        );

        printerTxtTable.print();

        return this;
    }
}
