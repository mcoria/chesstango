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

        printerTxtTable.setTitles("Move", "RNodes", "INodes", "QNodes", "LNodes", "TNodes", "LoNodes", "ENode", "Nodes");
        reportModel.nodesModelDetails.forEach(row -> {
            printerTxtTable.addRow(
                    row.move,
                    Long.toString(row.rootNodeCounter),
                    Long.toString(row.interiorNodeCounter),
                    Long.toString(row.quiescenceNodeCounter),
                    Long.toString(row.leafNodeCounter),
                    Long.toString(row.terminalNodeCounter),
                    Long.toString(row.loopNodeCounter),
                    Long.toString(row.egtbNodeCounter),
                    Long.toString(row.nodeCounter)
            );
        });

        printerTxtTable.setBottomRow(
                "SUM",
                Long.toString(reportModel.rootNodeCounterTotal),
                Long.toString(reportModel.interiorNodeCounterTotal),
                Long.toString(reportModel.quiescenceNodeCounterTotal),
                Long.toString(reportModel.leafNodeCounterTotal),
                Long.toString(reportModel.terminalNodeCounterTotal),
                Long.toString(reportModel.loopNodeCounterTotal),
                Long.toString(reportModel.egtbNodeCounterTotal),
                Long.toString(reportModel.nodeCounterTotal)
        );

        printerTxtTable.print();

        return this;
    }

}
