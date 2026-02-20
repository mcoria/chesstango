package net.chesstango.reports.search.transposition;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TranspositionReport implements Report {

    @Setter
    @Accessors(chain = true)
    private TranspositionModel transpositionModel;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "TranspositionReport";

    private PrintStream out;

    @Override
    public TranspositionReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        new HeaderPrinter()
                .setTranspositionModel(transpositionModel)
                .setOut(out)
                .print();

        new TranspositionPrinter()
                .setTranspositionModel(transpositionModel)
                .setOut(out)
                .print();

    }

    public TranspositionReport withMoveResults(List<SearchResult> searchResults) {
        this.transpositionModel = new TranspositionModel().collectStatistics(this.reportTitle, searchResults);
        return this;
    }
}
