package net.chesstango.reports.search.nodes.types;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
class NodesTypesPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private NodesTypesModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;


    @Override
    public NodesTypesPrinter print() {
        PrinterTxtTable printerTxtTable = new PrinterTxtTable(9).setOut(out);

        printerTxtTable.setTitles("Move", "RoNodes", "InNodes", "QNodes", "LeNodes", "TeNodes", "LoNodes", "EgNode", "Nodes");
        reportModel.nodesModelDetails.forEach(row -> {
            printerTxtTable.addRow(
                    row.move,
                    Long.toString(row.rootNodeCounter),
                    String.format("%d (%2d%%)", row.interiorNodeCounter, row.interiorNodeCounterPercentage),
                    String.format("%d (%2d%%)", row.quiescenceNodeCounter, row.quiescenceNodeCounterPercentage),
                    String.format("%d (%2d%%)", row.leafNodeCounter, row.leafNodeCounterPercentage),
                    Long.toString(row.terminalNodeCounter),
                    Long.toString(row.loopNodeCounter),
                    Long.toString(row.egtbNodeCounter),
                    Long.toString(row.nodeCounter)
            );
        });

        printerTxtTable.setBottomRow(
                "SUM",
                Long.toString(reportModel.rootNodeCounterTotal),
                String.format("%d (%2d%%)", reportModel.interiorNodeCounterTotal, reportModel.interiorNodeCounterPercentage),
                String.format("%d (%2d%%)", reportModel.quiescenceNodeCounterTotal, reportModel.quiescenceNodeCounterPercentage),
                String.format("%d (%2d%%)", reportModel.leafNodeCounterTotal, reportModel.leafNodeCounterPercentage),
                Long.toString(reportModel.terminalNodeCounterTotal),
                Long.toString(reportModel.loopNodeCounterTotal),
                Long.toString(reportModel.egtbNodeCounterTotal),
                Long.toString(reportModel.nodeCounterTotal)
        );

        printerTxtTable.print();

        return this;
    }

}
