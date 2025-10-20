package net.chesstango.reports.detail.evaluation;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EvaluationReport implements Report {
    private boolean exportEvaluations;
    private boolean printEvaluationsStatistics;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "EvaluationReport";

    @Setter
    @Accessors(chain = true)
    private EvaluationModel reportModel;

    private PrintStream out;

    @Override
    public EvaluationReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    private void print() {
        printSummary();

        if (printEvaluationsStatistics) {
            new EvaluationPrinter(out, reportModel)
                    .printEvaluationsStatistics();
        }

        if (exportEvaluations) {
            new ExportEvaluations(reportModel)
                    .exportEvaluations();
        }

    }

    private void printSummary() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        out.printf("EvaluationReport: %s\n\n", reportModel.reportTitle);
        out.printf("Evaluations           : %8d\n", reportModel.evaluationCounterTotal);
        out.printf("Positions             : %8d\n", reportModel.evaluationPositionCounterTotal);
        out.printf("Values                : %8d\n", reportModel.evaluationValueCounterTotal);
        out.printf("Collisions            : %8d (%2d%%)\n", reportModel.evaluationPositionValueCollisionsCounterTotal, reportModel.evaluationCollisionPercentageTotal);
        out.print("\n");
    }


    public EvaluationReport withEvaluationsStatistics() {
        this.printEvaluationsStatistics = true;
        return this;
    }

    public EvaluationReport withExportEvaluations() {
        this.exportEvaluations = true;
        return this;
    }

    public EvaluationReport withMoveResults(List<SearchResult> searchResults) {
        this.reportModel = EvaluationModel.collectStatistics(this.reportTitle, searchResults);
        return this;
    }


}
