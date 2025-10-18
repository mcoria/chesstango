package net.chesstango.reports.detail;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.reports.detail.evaluation.EvaluationReport;
import net.chesstango.reports.detail.nodes.NodesReport;
import net.chesstango.reports.detail.pv.PrincipalVariationReport;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesDetailReport implements Report {
    private boolean withPrincipalVariationReport;
    private boolean withEvaluationReport;
    private boolean withCutoffStatistics;
    private boolean withNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "SearchesDetailReport";

    private List<SearchResult> searchResultList;

    @Override
    public SearchesDetailReport printReport(PrintStream out) {
        if (withCutoffStatistics || withNodesVisitedStatistics) {
            NodesReport nodesReport = new NodesReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList);

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
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }

        if (withPrincipalVariationReport) {
            new PrincipalVariationReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }
        return this;
    }

    public SearchesDetailReport withMoveResults(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;
        return this;
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
}
