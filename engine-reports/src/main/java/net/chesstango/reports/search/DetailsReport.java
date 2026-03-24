package net.chesstango.reports.search;


import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.reports.Report;
import net.chesstango.reports.search.board.BoardReport;
import net.chesstango.reports.search.evaluation.EvaluationReport;
import net.chesstango.reports.search.evaluation.cache.EvaluationCacheReport;
import net.chesstango.reports.search.iteration.IterationEvaluationReport;
import net.chesstango.reports.search.nodes.depth.NodesDepthReport;
import net.chesstango.reports.search.nodes.ebf.EbfReport;
import net.chesstango.reports.search.nodes.types.NodesTypesReport;
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
    private boolean withBoardReport;
    private boolean withNodesDepthStatistics;
    private boolean withNodesTypesStatistics;
    private boolean withCutoffStatistics;
    private boolean withIterationEvaluationReport;
    private boolean withPrincipalVariationReport;
    private boolean withTranspositionReport;
    private boolean withEvaluationReport;
    private boolean withEvaluationCacheReport;
    private boolean withEbf;

    @Setter
    @Accessors(chain = true)
    private String reportTitle = "DetailsReport";

    private List<SearchResult> searchResultList;

    @Override
    public DetailsReport printReport(PrintStream out) {
        if (withBoardReport) {
            new BoardReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }

        if (withCutoffStatistics || withNodesDepthStatistics) {
            NodesDepthReport nodesReport = new NodesDepthReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList);

            if (withCutoffStatistics) {
                nodesReport.withCutoffStatistics();
            }
            if (withNodesDepthStatistics) {
                nodesReport.withNodesVisitedStatistics();
            }
            nodesReport.printReport(out);
        }

        if (withNodesTypesStatistics) {
            new NodesTypesReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }

        if (withEbf) {
            new EbfReport()
                    .setReportTitle(reportTitle)
                    .withMoveResults(searchResultList)
                    .printReport(out);
        }

        if (withIterationEvaluationReport) {
            new IterationEvaluationReport()
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

        if (withEvaluationCacheReport) {
            new EvaluationCacheReport()
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

    public DetailsReport withBoardReport() {
        this.withBoardReport = true;
        return this;
    }

    public DetailsReport withCutoffStatistics() {
        this.withCutoffStatistics = true;
        return this;
    }

    public DetailsReport withNodesDepthStatistics() {
        this.withNodesDepthStatistics = true;
        return this;
    }

    public DetailsReport withNodesTypesStatistics() {
        this.withNodesTypesStatistics = true;
        return this;
    }

    public DetailsReport withIterationEvaluationReport() {
        this.withIterationEvaluationReport = true;
        return this;
    }

    public DetailsReport withPrincipalVariationReport() {
        this.withPrincipalVariationReport = true;
        return this;
    }

    public DetailsReport withEvaluationReport() {
        this.withEvaluationReport = true;
        return this;
    }

    public DetailsReport withEvaluationCacheReport() {
        this.withEvaluationCacheReport = true;
        return this;
    }

    public DetailsReport withTranspositionReport() {
        this.withTranspositionReport = true;
        return this;
    }

    public DetailsReport withEbf() {
        this.withEbf = true;
        return this;
    }
}
