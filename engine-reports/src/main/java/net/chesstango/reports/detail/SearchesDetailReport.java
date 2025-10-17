package net.chesstango.reports.detail;


import net.chesstango.reports.Report;
import net.chesstango.reports.detail.evaluation.EvaluationReport;
import net.chesstango.reports.detail.nodes.NodesReport;
import net.chesstango.reports.detail.pv.PrincipalVariationReport;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesDetailReport implements Report {
    private final List<ReportAggregator> reportData = new LinkedList<>();

    private boolean withPrincipalVariationReport;
    private boolean withEvaluationReport;
    private boolean withCutoffStatistics;
    private boolean withNodesVisitedStatistics;

    @Override
    public SearchesDetailReport printReport(PrintStream out) {
        reportData.forEach(reportModel -> {

            if (withCutoffStatistics || withNodesVisitedStatistics) {
                NodesReport nodesReport = new NodesReport()
                        .setReportTitle(reportModel.reportTitle())
                        .withMoveResults(reportModel.searchResultList());

                if (withCutoffStatistics) {
                    nodesReport.withCutoffStatistics();
                }
                if (withNodesVisitedStatistics) {
                    nodesReport.withNodesVisitedStatistics();
                }
                nodesReport.printReport(out);
            }

            if (withEvaluationReport) {
                new EvaluationReport()
                        .withMoveResults(reportModel.searchResultList())
                        .setReportTitle(reportModel.reportTitle())
                        .printReport(out);
            }

            if (withPrincipalVariationReport) {
                new PrincipalVariationReport()
                        .withMoveResults(reportModel.searchResultList())
                        .setReportTitle(reportModel.reportTitle());
            }

        });
        return this;
    }

    public void addReportAggregator(String reportTitle, List<SearchResult> searchResultList) {
        reportData.add(new ReportAggregator(reportTitle, searchResultList));
    }


    public SearchesDetailReport withCutoffStatistics() {
        this.withCutoffStatistics = true;
        return this;
    }

    public SearchesDetailReport withNodesVisitedStatistics() {
        this.withNodesVisitedStatistics = true;
        return this;
    }


    public SearchesDetailReport withEvaluationReport() {
        this.withEvaluationReport = true;
        return this;
    }

    public SearchesDetailReport withPrincipalVariationReport() {
        this.withPrincipalVariationReport = true;
        return this;
    }

    private record ReportAggregator(String reportTitle, List<SearchResult> searchResultList) {
    }
}
