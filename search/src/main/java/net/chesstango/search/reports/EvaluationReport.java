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
    private boolean exportEvaluations;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "EvaluationReport";

    @Setter
    @Accessors(chain = true)
    private EvaluationReportModel reportModel;

    private PrintStream out;

    public EvaluationReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        printSummary();

        new PrintEvaluationsStatistics(out, reportModel).printEvaluationsStatistics();

        if (exportEvaluations) {
            new ExportEvaluations(reportModel).exportEvaluations();
        }

    }

    private void printSummary() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("NodesReport: %s\n\n", reportModel.reportTitle);
        out.printf("Evaluations           : %8d\n", reportModel.evaluationCounterTotal);
        out.printf("Positions             : %8d\n", reportModel.evaluationPositionCounterTotal);
        out.printf("Values                : %8d\n", reportModel.evaluationValueCounterTotal);
        out.printf("Collisions            : %8d (%2d%%)\n", reportModel.evaluationPositionValueCollisionsCounterTotal, reportModel.evaluationCollisionPercentageTotal);
        out.printf("\n");
    }


    public EvaluationReport withExportEvaluations() {
        this.exportEvaluations = true;
        return this;
    }

    public EvaluationReport withMoveResults(List<SearchMoveResult> searchMoveResults) {
        this.reportModel = EvaluationReportModel.collectStatistics(this.reportTitle, searchMoveResults);
        return this;
    }
}
