package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.reports.search.evaluation.EvaluationReport;
import net.chesstango.reports.search.nodes.NodesReport;
import net.chesstango.reports.search.pv.PrincipalVariationReport;
import net.chesstango.reports.search.transposition.TranspositionReport;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class DetailsReport implements Report {
    private boolean withNodesVisitedStatistics;
    private boolean withCutoffStatistics;
    private boolean withPrincipalVariationReport;
    private boolean withTranspositionReport;
    private boolean withEvaluationReport;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "DetailsReport";

    private List<SearchResult> searchResultList;

    @Override
    public DetailsReport printReport(PrintStream out) {
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

        if (withPrincipalVariationReport) {
            new PrincipalVariationReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }

        if (withTranspositionReport) {
            new TranspositionReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }

        if (withEvaluationReport) {
            new EvaluationReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }
        return this;
    }

    public DetailsReport withMoveResults(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;
        return this;
    }

    public DetailsReport withCutoffStatistics() {
        this.withCutoffStatistics = true;
        return this;
    }

    public DetailsReport withNodesVisitedStatistics() {
        this.withNodesVisitedStatistics = true;
        return this;
    }

    public DetailsReport withEvaluationReport() {
        this.withEvaluationReport = true;
        return this;
    }

    public DetailsReport withPrincipalVariationReport() {
        this.withPrincipalVariationReport = true;
        return this;
    }

    public DetailsReport withTranspositionReport() {
        this.withTranspositionReport = true;
        return this;
    }
}
