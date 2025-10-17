package net.chesstango.reports.detail;


import net.chesstango.reports.Report;
import net.chesstango.reports.detail.evaluation.EvaluationReport;
import net.chesstango.reports.detail.evaluation.EvaluationModel;
import net.chesstango.reports.detail.nodes.NodesReport;
import net.chesstango.reports.detail.nodes.NodesModel;
import net.chesstango.reports.detail.pv.PrincipalVariationReport;
import net.chesstango.reports.detail.pv.PrincipalVariationModel;
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
    private final EvaluationReport evaluationReport = new EvaluationReport();
    private final NodesReport nodesReport = new NodesReport();
    private final PrincipalVariationReport principalVariationReport = new PrincipalVariationReport();

    private final List<ReportAggregator> reportModels = new LinkedList<>();

    private boolean withPrincipalVariation;
    private boolean withEvaluationReport;
    private boolean withNodesReport;

    @Override
    public SearchesDetailReport printReport(PrintStream out) {
        reportModels.forEach(reportModel -> {
            if (withNodesReport) {
                nodesReport.setReportModel(reportModel.nodesModel())
                        .printReport(out);
            }

            if (withEvaluationReport) {
                evaluationReport.setReportModel(reportModel.evaluationModel())
                        .printReport(out);
            }

            if (withPrincipalVariation) {
                principalVariationReport.setReportModel(reportModel.principalVariationModel())
                        .printReport(out);
            }
        });
        return this;
    }

    public void addReportAggregator(String reportTitle, List<SearchResult> searchResultList) {
        NodesModel nodesModel = NodesModel.collectStatistics(reportTitle, searchResultList);
        EvaluationModel evaluationModel = EvaluationModel.collectStatistics(reportTitle, searchResultList);
        PrincipalVariationModel principalVariationModel = PrincipalVariationModel.collectStatics(reportTitle, searchResultList);
        reportModels.add(new ReportAggregator(nodesModel, evaluationModel, principalVariationModel));
    }


    public SearchesDetailReport withCutoffStatistics() {
        nodesReport.withCutoffStatistics();
        this.withNodesReport = true;
        return this;
    }

    public SearchesDetailReport withNodesVisitedStatistics() {
        nodesReport.withNodesVisitedStatistics();
        this.withNodesReport = true;
        return this;
    }


    public SearchesDetailReport withEvaluationReport() {
        this.withEvaluationReport = true;
        return this;
    }

    public SearchesDetailReport withPrincipalVariation() {
        this.withPrincipalVariation = true;
        return this;
    }

    private record ReportAggregator(NodesModel nodesModel,
                                    EvaluationModel evaluationModel,
                                    PrincipalVariationModel principalVariationModel) {
    }
}
