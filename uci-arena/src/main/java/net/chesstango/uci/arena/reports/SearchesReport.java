package net.chesstango.uci.arena.reports;

import net.chesstango.search.reports.EvaluationReport;
import net.chesstango.search.reports.EvaluationReportModel;
import net.chesstango.search.reports.NodesReport;
import net.chesstango.search.reports.NodesReportModel;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesReport {
    private final NodesReport nodesReport = new NodesReport();
    private final EvaluationReport evaluationReport = new EvaluationReport();
    private final List<ReportModels> reportModels = new LinkedList<>();

    public SearchesReport printReport(PrintStream out) {
        reportModels.forEach(reportModel -> {
            nodesReport.setReportModel(reportModel.nodesReportModel())
                    .printReport(out);

            evaluationReport.setReportModel(reportModel.evaluationReportModel())
                    .printReport(out);
        });
        return this;
    }

    public SearchesReport withMathResults(List<EngineController> enginesOrder, List<MatchResult> matchResult) {
        enginesOrder.forEach(engineController -> {
            matchResult.stream()
                    .filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null)
                    .forEach(result -> {
                        NodesReportModel nodesReportModel = NodesReportModel.collectStatistics(String.format("%s - %s", engineController.getEngineName(), result.getMathId()), result.getSessionWhite().getSearches());
                        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatistics(String.format("%s - %s", engineController.getEngineName(), result.getMathId()), result.getSessionWhite().getSearches());
                        reportModels.add(new ReportModels(nodesReportModel, evaluationReportModel));
                    });

            matchResult.stream()
                    .filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null)
                    .forEach(result -> {
                        NodesReportModel nodesReportModel = NodesReportModel.collectStatistics(String.format("%s - %s", engineController.getEngineName(), result.getMathId()), result.getSessionBlack().getSearches());
                        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatistics(String.format("%s - %s", engineController.getEngineName(), result.getMathId()), result.getSessionBlack().getSearches());
                        reportModels.add(new ReportModels(nodesReportModel, evaluationReportModel));
                    });


        });
        return this;
    }


    public SearchesReport withCutoffStatistics() {
        nodesReport.withCutoffStatistics();
        return this;
    }

    public SearchesReport withNodesVisitedStatistics() {
        nodesReport.withNodesVisitedStatistics();
        return this;
    }

    public SearchesReport withPrincipalVariation() {
        throw new RuntimeException("Unsupported withPrincipalVariation option");
        //searchesReport.withPrincipalVariation();
        //return this;
    }

    private record ReportModels(NodesReportModel nodesReportModel, EvaluationReportModel evaluationReportModel){};
}
