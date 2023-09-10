package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.NodeStatistics;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintCollisionStatistics;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintCutoffStatics;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintMovesPerLevelStatics;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintNodesVisitedStatistics;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SessionReport {
    private final List<SessionReportModel> sessionReportModels = new ArrayList<>();
    private boolean printCollisionStatics;
    private boolean printNodesVisitedStatics;
    private boolean printMovesPerLevelStatics;
    private boolean printCutoffStatics;
    private boolean breakByColor;
    private PrintStream out;

    public SessionReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public SessionReport withMathResults(List<EngineController> enginesOrder, List<GameResult> matchResult) {
        enginesOrder.forEach(engineController -> {
            List<Session> sessionsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(GameResult::getSessionWhite).collect(Collectors.toList());
            List<Session> sessionsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(GameResult::getSessionBlack).collect(Collectors.toList());

            List<SearchMoveResult> searchesWhite = sessionsWhite.stream().map(Session::getSearches).flatMap(List::stream).collect(Collectors.toList());
            List<SearchMoveResult> searchesBlack = sessionsBlack.stream().map(Session::getSearches).flatMap(List::stream).collect(Collectors.toList());

            if (breakByColor) {
                if (searchesWhite.size() > 0) {
                    sessionReportModels.add(collectStatics(String.format("%s white", engineController.getEngineName()), searchesWhite));
                }
                if (searchesBlack.size() > 0) {
                    sessionReportModels.add(collectStatics(String.format("%s black", engineController.getEngineName()), searchesBlack));
                }
            } else {
                List<SearchMoveResult> searches = new ArrayList<>();
                searches.addAll(searchesWhite);
                searches.addAll(searchesBlack);
                
                if (searches.size() > 0) {
                    sessionReportModels.add(collectStatics(engineController.getEngineName(), searches));
                }
            }
        });

        return this;
    }

    private SessionReportModel collectStatics(String engineName, List<SearchMoveResult> searches) {
        SessionReportModel rowModel = new SessionReportModel();
        rowModel.engineName = engineName;

        rowModel.searches = searches.stream().count();
        rowModel.searchesWithoutCollisions = searches.stream().mapToInt(SearchMoveResult::getBestMovesCounter).filter(value -> value == 0).count();
        rowModel.searchesWithoutCollisionsPercentage = (int) ((rowModel.searchesWithoutCollisions * 100) / rowModel.searches);
        rowModel.searchesWithCollisions = searches.stream().mapToInt(SearchMoveResult::getBestMovesCounter).filter(value -> value > 0).count();
        rowModel.searchesWithCollisionsPercentage = (int) ((rowModel.searchesWithCollisions * 100) / rowModel.searches);

        if (rowModel.searchesWithCollisions > 0) {
            rowModel.avgOptionsPerCollision = searches.stream().mapToInt(SearchMoveResult::getBestMovesCounter).filter(value -> value > 0).average().getAsDouble();
        }


        long[] expectedRNodesCounters = new long[30];
        rowModel.visitedRNodesCounters = new long[30];
        rowModel.visitedQNodesCounters = new int[30];
        rowModel.visitedRNodesCountersAvg = new int[30];
        rowModel.visitedQNodesCountersAvg = new int[30];
        rowModel.cutoffPercentages = new int[30];
        rowModel.maxDistinctMovesPerLevel = new int[30];
        rowModel.maxSearchRLevel = 0;
        rowModel.visitedNodesTotal = 0;


        searches.forEach(searchMoveResult -> {
            NodeStatistics regularNodeStatistics = searchMoveResult.getRegularNodeStatistics();
            int[] visitedRNodeCounters = regularNodeStatistics.visitedNodesCounters();
            int[] expectedRNodeCounters = regularNodeStatistics.expectedNodesCounters();
            for (int i = 0; i < visitedRNodeCounters.length; i++) {
                rowModel.visitedRNodesCounters[i] += visitedRNodeCounters[i];
                expectedRNodesCounters[i] += expectedRNodeCounters[i];

            }

            NodeStatistics quiescenceNodeStatistics = searchMoveResult.getQuiescenceNodeStatistics();
            int[] visitedQNodesCounters = quiescenceNodeStatistics.visitedNodesCounters();
            for (int i = 0; i < visitedQNodesCounters.length; i++) {
                rowModel.visitedQNodesCounters[i] += visitedQNodesCounters[i];
            }

            /*
            if (searchMoveResult.getDistinctMovesPerLevel() != null) {
                int level = 0;
                for (Set<Move> moveCollection :
                        searchMoveResult.getDistinctMovesPerLevel()) {
                    if (rowModel.maxDistinctMovesPerLevel[level] < moveCollection.size()) {
                        rowModel.maxDistinctMovesPerLevel[level] = moveCollection.size();
                    }
                    level++;
                }
            }
             */
        });

        for (int i = 0; i < 30; i++) {
            if (rowModel.visitedRNodesCounters[i] > 0) {
                rowModel.cutoffPercentages[i] = (int) (100 - (100 * rowModel.visitedRNodesCounters[i] / expectedRNodesCounters[i]));
                rowModel.maxSearchRLevel = i + 1;
            }

            if (rowModel.visitedQNodesCounters[i] > 0) {
                rowModel.maxSearchQLevel = i + 1;
            }

            rowModel.visitedRNodesTotal += rowModel.visitedRNodesCounters[i];
            rowModel.visitedQNodesTotal += rowModel.visitedQNodesCounters[i];
            rowModel.visitedRNodesCountersAvg[i] = (int) (rowModel.visitedRNodesCounters[i] / rowModel.searches);
            rowModel.visitedQNodesCountersAvg[i] = (int) (rowModel.visitedQNodesCounters[i] / rowModel.searches);
        }

        rowModel.visitedNodesTotal = rowModel.visitedRNodesTotal + rowModel.visitedQNodesTotal;
        rowModel.visitedNodesTotalAvg = (int) (rowModel.visitedNodesTotal / rowModel.searches);

        rowModel.visitedRNodesAvg = (int) (rowModel.visitedRNodesTotal/ rowModel.searches);
        rowModel.visitedQNodesAvg = (int) (rowModel.visitedQNodesTotal/ rowModel.searches);

        return rowModel;
    }

    private void print() {
        AtomicInteger maxRLevelVisited = new AtomicInteger();
        AtomicInteger maxQLevelVisited = new AtomicInteger();

        for (SessionReportModel sessionReportModel : sessionReportModels) {
            if (maxRLevelVisited.get() < sessionReportModel.maxSearchRLevel) {
                maxRLevelVisited.set(sessionReportModel.maxSearchRLevel);
            }
            if (maxQLevelVisited.get() < sessionReportModel.maxSearchQLevel) {
                maxQLevelVisited.set(sessionReportModel.maxSearchQLevel);
            }
        }


        if (printCollisionStatics) {
            new PrintCollisionStatistics(out, sessionReportModels).printCollisionStatistics();
        }

        if (printNodesVisitedStatics) {
            new PrintNodesVisitedStatistics(out, sessionReportModels)
                    .printNodesVisitedStaticsByType()
                    .printNodesVisitedStatics(maxRLevelVisited, maxQLevelVisited)
                    .printNodesVisitedStaticsAvg(maxRLevelVisited, maxQLevelVisited);
        }

        if (printMovesPerLevelStatics) {
            new PrintMovesPerLevelStatics(out, sessionReportModels)
                    .printMovesPerLevelStatics(maxRLevelVisited);
        }

        if (printCutoffStatics) {
            new PrintCutoffStatics(out, sessionReportModels).printCutoffStatics(maxRLevelVisited);
        }

    }


    public SessionReport withCollisionStatics() {
        this.printCollisionStatics = true;
        return this;
    }

    public SessionReport withNodesVisitedStatics() {
        this.printNodesVisitedStatics = true;
        return this;
    }

    public SessionReport withMovesPerLevelStatics() {
        this.printMovesPerLevelStatics = true;
        return this;
    }

    public SessionReport withCutoffStatics() {
        this.printCutoffStatics = true;
        return this;
    }

    public SessionReport breakByColor() {
        this.breakByColor = true;
        return this;
    }

}
