package net.chesstango.uci.arena.reports;

import net.chesstango.search.reports.NodesReport;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

import net.chesstango.search.reports.NodesReportModel;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesPerGameReport {

    private NodesReport nodesReport = new NodesReport();

    public void printTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {
        List<NodesReportModel> reportRows = new ArrayList<>();

        enginesOrder.forEach(engineController -> {
            matchResult.stream()
                    .filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null)
                    .map(result -> NodesReportModel.collectStatistics(engineController.getEngineName(), result.getSessionWhite().getSearches()))
                    .forEach(reportRows::add);

            matchResult.stream()
                    .filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null)
                    .map(result -> NodesReportModel.collectStatistics(engineController.getEngineName(), result.getSessionBlack().getSearches()))
                    .forEach(reportRows::add);

        });

        reportRows.forEach(searchesReportModel -> {
            nodesReport.setReportModel(searchesReportModel);
            nodesReport.printReport(System.out);
        });
    }


    public SearchesPerGameReport withCutoffStatics() {
        nodesReport.withCutoffStatics();
        return this;
    }

    public SearchesPerGameReport withNodesVisitedStatics() {
        nodesReport.withNodesVisitedStatics();
        return this;
    }

    public SearchesPerGameReport withPrincipalVariation() {
        //searchesReport.withPrincipalVariation();
        return this;
    }

}
