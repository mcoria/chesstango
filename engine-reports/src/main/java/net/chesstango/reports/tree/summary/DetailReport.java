package net.chesstango.reports.tree.summary;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.reports.tree.evaluation.EvaluationReport;
import net.chesstango.reports.tree.nodes.NodesReport;
import net.chesstango.reports.tree.pv.PrincipalVariationReport;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class DetailReport implements Report {
    private boolean withPrincipalVariationReport;
    private boolean withEvaluationReport;
    private boolean withCutoffStatistics;
    private boolean withNodesVisitedStatistics;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "DetailReport";

    private List<SearchResult> searchResultList;

    @Override
    public DetailReport printReport(PrintStream out) {
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

    public DetailReport withMoveResults(List<SearchResult> searchResultList) {
        this.searchResultList = searchResultList;
        return this;
    }

    public DetailReport withCutoffStatistics() {
        this.withCutoffStatistics = true;
        return this;
    }

    public DetailReport withNodesVisitedStatistics() {
        this.withNodesVisitedStatistics = true;
        return this;
    }

    public DetailReport withEvaluationReport() {
        this.withEvaluationReport = true;
        return this;
    }

    public DetailReport withPrincipalVariationReport() {
        this.withPrincipalVariationReport = true;
        return this;
    }
}
