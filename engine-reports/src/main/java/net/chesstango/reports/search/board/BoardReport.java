package net.chesstango.reports.search.board;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BoardReport implements Report {

    @Setter
    @Accessors(chain = true)
    private BoardModel boardModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "BoardReport";

    private PrintStream out;

    @Override
    public BoardReport printReport(PrintStream out) {
        this.out = out;
        print();
        return this;
    }

    public BoardReport withMoveResults(List<SearchResult> searchResults) {
        this.boardModel = new BoardModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }

    void print() {
        new HeaderPrinter()
                .setReportModel(boardModel)
                .setOut(out)
                .print();


        new BoardPrinter()
                .setReportModel(boardModel)
                .setOut(out)
                .print();
    }
}
