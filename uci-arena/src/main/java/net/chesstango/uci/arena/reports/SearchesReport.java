package net.chesstango.uci.arena.reports;

import net.chesstango.search.reports.NodesReport;
import net.chesstango.search.reports.NodesReportModel;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesReport {
    private final NodesReport nodesReport = new NodesReport();
    private final List<NodesReportModel> nodesReportModels = new ArrayList<>();

    public SearchesReport printReport(PrintStream out) {
        nodesReportModels.forEach(searchesReportModel -> {
            nodesReport.setReportModel(searchesReportModel);
            nodesReport.printReport(out);
        });
        return this;
    }

    public SearchesReport withMathResults(List<EngineController> enginesOrder, List<MatchResult> matchResult) {
        enginesOrder.forEach(engineController -> {
            matchResult.stream()
                    .filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null)
                    .map(result -> NodesReportModel.collectStatistics(String.format("%s - %s", engineController.getEngineName(), result.getMathId()), result.getSessionWhite().getSearches()))
                    .forEach(nodesReportModels::add);

            matchResult.stream()
                    .filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null)
                    .map(result -> NodesReportModel.collectStatistics(String.format("%s - %s", engineController.getEngineName(), result.getMathId()), result.getSessionBlack().getSearches()))
                    .forEach(nodesReportModels::add);

        });
        return this;
    }


    public SearchesReport withCutoffStatics() {
        nodesReport.withCutoffStatics();
        return this;
    }

    public SearchesReport withNodesVisitedStatics() {
        nodesReport.withNodesVisitedStatics();
        return this;
    }

    public SearchesReport withPrincipalVariation() {
        throw new RuntimeException("Unsupported withPrincipalVariation option");
        //searchesReport.withPrincipalVariation();
        //return this;
    }
}
