package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.reports.evaluationreport_ui.ExportEvaluations;
import net.chesstango.search.reports.evaluationreport_ui.PrintEvaluationsStatistics;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EvaluationReport {
    private boolean printEvaluationsStatistics;
    private boolean exportEvaluations;

    @Setter
    @Accessors(chain = true)
    private String engineName = "Tango";

    @Setter
    @Accessors(chain = true)
    private EvaluationReportModel reportModel;

    private PrintStream out;

    public EvaluationReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    protected void print() {
        printSummary();

        new PrintEvaluationsStatistics(out, reportModel).printEvaluationsStatistics();

        if (exportEvaluations) {
            new ExportEvaluations(reportModel).exportEvaluations();
        }

    }

    private void printSummary() {
        out.printf("----------------------------------------------------------------------------\n\n");
        out.printf("Moves played by engine: %s\n", reportModel.engineName);
        out.printf("Evaluated        Nodes: %8d\n", reportModel.evaluatedGamesCounterTotal);
        out.printf("\n");
    }

    public EvaluationReport withEvaluationsStatics() {
        this.printEvaluationsStatistics = true;
        return this;
    }


    public EvaluationReport withExportEvaluations() {
        this.exportEvaluations = true;
        return this;
    }

    public EvaluationReport withMoveResults(List<SearchMoveResult> searchMoveResults) {
        this.reportModel = EvaluationReportModel.collectStatics(this.engineName, searchMoveResults);
        return this;
    }
}
