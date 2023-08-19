package net.chesstango.uci.arena.reports;

import net.chesstango.search.reports.SearchesReport;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.reports.SearchesReport.ReportModel;

/**
 * Por cada juego de Tango muestra estadísticas de cada búsqueda.
 *
 * @author Mauricio Coria
 */
public class SearchesPerGameReport {

    private SearchesReport searchesReport = new SearchesReport();

    public void printTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {
        List<ReportModel> reportRows = new ArrayList<>();

        enginesOrder.forEach(engineController -> {
            matchResult.stream()
                    .filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null)
                    .map(result -> searchesReport.collectStatics(engineController.getEngineName(), result.getSessionWhite().getSearches()))
                    .forEach(reportRows::add);

            matchResult.stream()
                    .filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null)
                    .map(result -> searchesReport.collectStatics(engineController.getEngineName(), result.getSessionBlack().getSearches()))
                    .forEach(reportRows::add);

        });

        reportRows.forEach(reportModel -> {
            searchesReport.setReport(reportModel);
            searchesReport.printReport(System.out);
        });
    }


    public SearchesPerGameReport withCutoffStatics() {
        searchesReport.withCutoffStatics();
        return this;
    }

    public SearchesPerGameReport withNodesVisitedStatics() {
        searchesReport.withNodesVisitedStatics();
        return this;
    }

    public SearchesPerGameReport withPrincipalVariation() {
        searchesReport.withPrincipalVariation();
        return this;
    }

}
