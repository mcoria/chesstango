package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Printer;
import net.chesstango.reports.PrinterTxtTable;
import net.chesstango.reports.search.nodes.types.NodesTypesModel;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
class SummaryNodesTypesPrinter implements Printer {
    private List<NodesTypesModel> reportRows;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    public SummaryNodesTypesPrinter setReportRows(List<NodesTypesModel> reportRows) {
        this.reportRows = reportRows;
        return this;
    }

    @Override
    public SummaryNodesTypesPrinter print() {
        out.printf("%n Nodes visited per type %n");

        PrinterTxtTable printerTxtTable = new PrinterTxtTable(10).setOut(out);

        printerTxtTable.setTitles("ENGINE NAME", "SEARCHES", "RoNodes", "InNodes", "QNodes", "LeNodes", "TeNodes", "LoNodes", "EgNode", "Nodes");
        reportRows.forEach(row -> {
            printerTxtTable.addRow(row.searchGroupName,
                    Integer.toString(row.searches),
                    Long.toString(row.rootNodeCounterTotal),
                    Long.toString(row.interiorNodeCounterTotal),
                    Long.toString(row.quiescenceNodeCounterTotal),
                    Long.toString(row.leafNodeCounterTotal),
                    Long.toString(row.terminalNodeCounterTotal),
                    Long.toString(row.loopNodeCounterTotal),
                    Long.toString(row.egtbNodeCounterTotal),
                    Long.toString(row.nodeCounterTotal)
            );
        });
        printerTxtTable.print();

        return this;
    }


}
