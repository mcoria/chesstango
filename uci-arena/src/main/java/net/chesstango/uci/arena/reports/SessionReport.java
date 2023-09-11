package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintCollisionStatistics;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintCutoffStatics;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintNodesVisitedStatistics;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SessionReport {
    private final List<SessionReportModel> sessionReportModels = new ArrayList<>();
    private boolean printCollisionStatistics;
    private boolean printNodesVisitedStatistics;
    private boolean printCutoffStatistics;
    private boolean breakByColor;
    private PrintStream out;

    public SessionReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public SessionReport withMathResults(List<EngineController> enginesOrder, List<MatchResult> matchResult) {
        enginesOrder.forEach(engineController -> {
            List<Session> sessionsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(MatchResult::getSessionWhite).collect(Collectors.toList());
            List<Session> sessionsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(MatchResult::getSessionBlack).collect(Collectors.toList());

            List<SearchMoveResult> searchesWhite = sessionsWhite.stream().map(Session::getSearches).flatMap(List::stream).collect(Collectors.toList());
            List<SearchMoveResult> searchesBlack = sessionsBlack.stream().map(Session::getSearches).flatMap(List::stream).collect(Collectors.toList());

            if (breakByColor) {
                if (searchesWhite.size() > 0) {
                    sessionReportModels.add(SessionReportModel.collectStatics(String.format("%s white", engineController.getEngineName()), searchesWhite));
                }
                if (searchesBlack.size() > 0) {
                    sessionReportModels.add(SessionReportModel.collectStatics(String.format("%s black", engineController.getEngineName()), searchesBlack));
                }
            } else {
                List<SearchMoveResult> searches = new ArrayList<>();
                searches.addAll(searchesWhite);
                searches.addAll(searchesBlack);

                if (searches.size() > 0) {
                    sessionReportModels.add(SessionReportModel.collectStatics(engineController.getEngineName(), searches));
                }
            }
        });

        return this;
    }

    private void print() {
        if (printCollisionStatistics) {
            new PrintCollisionStatistics(out, sessionReportModels).printCollisionStatistics();
        }

        if (printNodesVisitedStatistics) {
            new PrintNodesVisitedStatistics(out, sessionReportModels)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics()
                    .printNodesVisitedStaticsAvg();
        }


        if (printCutoffStatistics) {
            new PrintCutoffStatics(out, sessionReportModels)
                    .printCutoffStatics();
        }

    }


    public SessionReport withCollisionStatistics() {
        this.printCollisionStatistics = true;
        return this;
    }

    public SessionReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SessionReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public SessionReport breakByColor() {
        this.breakByColor = true;
        return this;
    }

}
