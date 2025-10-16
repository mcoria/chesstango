package net.chesstango.reports;


import net.chesstango.reports.evaluation.EvaluationReport;
import net.chesstango.reports.evaluation.EvaluationReportModel;
import net.chesstango.reports.nodes.NodesReport;
import net.chesstango.reports.nodes.NodesReportModel;
import net.chesstango.reports.pv.PrincipalVariationReport;
import net.chesstango.reports.pv.PrincipalVariationReportModel;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesByTreeReport {
    private final EvaluationReport evaluationReport = new EvaluationReport();
    private final NodesReport nodesReport = new NodesReport();
    private final PrincipalVariationReport principalVariationReport = new PrincipalVariationReport();

    private final List<ReportAggregator> reportModels = new LinkedList<>();

    private boolean withPrincipalVariation;
    private boolean withEvaluationReport;
    private boolean withNodesReport;

    public SearchesByTreeReport printReport(PrintStream out) {
        reportModels.forEach(reportModel -> {
            if (withNodesReport) {
                nodesReport.setReportModel(reportModel.nodesReportModel())
                        .printReport(out);
            }

            if (withEvaluationReport) {
                evaluationReport.setReportModel(reportModel.evaluationReportModel())
                        .printReport(out);
            }

            if (withPrincipalVariation) {
                principalVariationReport.setReportModel(reportModel.principalVariationReportModel())
                        .printReport(out);
            }
        });
        return this;
    }

    private void addReportAggregator(String reportTitle, List<SearchResult> searchResultList) {
        NodesReportModel nodesReportModel = NodesReportModel.collectStatistics(reportTitle, searchResultList);
        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatistics(reportTitle, searchResultList);
        PrincipalVariationReportModel principalVariationReportModel = PrincipalVariationReportModel.collectStatics(reportTitle, searchResultList);
        reportModels.add(new ReportAggregator(nodesReportModel, evaluationReportModel, principalVariationReportModel));
    }


    public SearchesByTreeReport withCutoffStatistics() {
        nodesReport.withCutoffStatistics();
        this.withNodesReport = true;
        return this;
    }

    public SearchesByTreeReport withNodesVisitedStatistics() {
        nodesReport.withNodesVisitedStatistics();
        this.withNodesReport = true;
        return this;
    }


    public SearchesByTreeReport withEvaluationReport() {
        this.withEvaluationReport = true;
        return this;
    }

    public SearchesByTreeReport withPrincipalVariation() {
        this.withPrincipalVariation = true;
        return this;
    }

    private record ReportAggregator(NodesReportModel nodesReportModel,
                                    EvaluationReportModel evaluationReportModel,
                                    PrincipalVariationReportModel principalVariationReportModel) {
    }
}
