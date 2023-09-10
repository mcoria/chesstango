package net.chesstango.uci.arena.reports;

import net.chesstango.search.reports.NodesReport;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import net.chesstango.search.reports.NodesReportModel;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesPerGameReport {
    private final NodesReport nodesReport = new NodesReport();
    private final List<NodesReportModel> nodesReportModels = new ArrayList<>();

    public SearchesPerGameReport printReport(PrintStream out) {
        nodesReportModels.forEach(searchesReportModel -> {
            nodesReport.setReportModel(searchesReportModel);
            nodesReport.printReport(out);
        });
        return this;
    }

    public SearchesPerGameReport withTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {
        enginesOrder.forEach(engineController -> {
            matchResult.stream()
                    .filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null)
                    .map(result -> NodesReportModel.collectStatistics(engineController.getEngineName(), result.getSessionWhite().getSearches()))
                    .forEach(nodesReportModels::add);

            matchResult.stream()
                    .filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null)
                    .map(result -> NodesReportModel.collectStatistics(engineController.getEngineName(), result.getSessionBlack().getSearches()))
                    .forEach(nodesReportModels::add);

        });
        return this;
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
        throw new RuntimeException("Unsupported withPrincipalVariation option");
        //searchesReport.withPrincipalVariation();
        //return this;
    }
}
