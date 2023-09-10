package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.NodeStatistics;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.reports.sessionreport_ui.PrintCollisionStatistics;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SessionReport {
    private boolean printCollisionStatics;
    private boolean printNodesVisitedStatics;
    private boolean printMovesPerLevelStatics;
    private boolean printCutoffStatics;
    private boolean breakByColor;
    private List<SessionReportModel> reportRows = new ArrayList<>();
    private PrintStream out;

    public SessionReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    public SessionReport withTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {
        enginesOrder.forEach(engineController -> {
            List<Session> sessionsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(GameResult::getSessionWhite).collect(Collectors.toList());
            List<Session> sessionsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(GameResult::getSessionBlack).collect(Collectors.toList());

            List<SearchMoveResult> searchesWhite = sessionsWhite.stream().map(Session::getSearches).flatMap(List::stream).collect(Collectors.toList());
            List<SearchMoveResult> searchesBlack = sessionsBlack.stream().map(Session::getSearches).flatMap(List::stream).collect(Collectors.toList());

            if (breakByColor) {
                if (searchesWhite.size() > 0) {
                    reportRows.add(collectStatics(String.format("%s white", engineController.getEngineName()), searchesWhite));
                }
                if (searchesBlack.size() > 0) {
                    reportRows.add(collectStatics(String.format("%s black", engineController.getEngineName()), searchesBlack));
                }
            } else {
                List<SearchMoveResult> searches = new ArrayList<>();
                searches.addAll(searchesWhite);
                searches.addAll(searchesBlack);
                
                if (searches.size() > 0) {
                    reportRows.add(collectStatics(engineController.getEngineName(), searches));
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

        for (SessionReportModel sessionReportModel : reportRows) {
            if (maxRLevelVisited.get() < sessionReportModel.maxSearchRLevel) {
                maxRLevelVisited.set(sessionReportModel.maxSearchRLevel);
            }
            if (maxQLevelVisited.get() < sessionReportModel.maxSearchQLevel) {
                maxQLevelVisited.set(sessionReportModel.maxSearchQLevel);
            }
        }


        if (printCollisionStatics) {
            new PrintCollisionStatistics(out, reportRows).printCollisionStatistics();
        }

        if (printNodesVisitedStatics) {
            printNodesVisitedStaticsByType(reportRows);
            printNodesVisitedStatics(maxRLevelVisited, maxQLevelVisited, reportRows);
            printNodesVisitedStaticsAvg(maxRLevelVisited, maxQLevelVisited, reportRows);
        }

        if (printMovesPerLevelStatics) {
            printMovesPerLevelStatics(maxRLevelVisited, reportRows);
        }

        if (printCutoffStatics) {
            printCutoffStatics(maxRLevelVisited, reportRows);
        }

    }


    private void printNodesVisitedStaticsByType(List<SessionReportModel> reportRows) {
        out.println("\n Nodes visited per type");

        // Marco superior de la tabla
        out.printf(" _____________________________________________________________________________________________________________________________________");
        out.printf("\n");

        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES |       RNodes |       QNodes |  Total Nodes |  AVG RNodes |  AVG QNodes |   AVG Nodes ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.engineName, row.searches);
            out.printf("| %12d ", row.visitedRNodesTotal);
            out.printf("| %12d ", row.visitedQNodesTotal);
            out.printf("| %12d ", row.visitedNodesTotal);
            out.printf("| %11d ", row.visitedRNodesAvg);
            out.printf("| %11d ", row.visitedQNodesAvg);
            out.printf("| %11d ", row.visitedNodesTotalAvg);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" -------------------------------------------------------------------------------------------------------------------------------------");
        out.printf("\n");
    }

    private void printNodesVisitedStatics(AtomicInteger maxRLevelVisited, AtomicInteger maxQLevelVisited, List<SessionReportModel> reportRows) {
        out.println("\n Nodes visited per search level");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("___________"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("____________"));
        out.printf("______________"); // Nodes
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| Total Nodes ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("| %8d ", row.visitedRNodesCounters[depth]));
            IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("| %9d ", row.visitedQNodesCounters[depth]));
            out.printf("| %11d ", row.visitedNodesTotal);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("------------"));
        out.printf("--------------"); // Total Nodes
        out.printf("\n");
    }

    private void printNodesVisitedStaticsAvg(AtomicInteger maxRLevelVisited, AtomicInteger maxQLevelVisited, List<SessionReportModel> reportRows) {
        out.println("\n Nodes visited per search level AVG");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("___________"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("____________"));
        out.printf("______________"); // AVG Nodes/S
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("| AVG Nodes/S ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("| %8d ", row.visitedRNodesCountersAvg[depth]));
            IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("| %9d ", row.visitedQNodesCountersAvg[depth]));
            out.printf("| %11d ", row.visitedNodesTotalAvg);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> out.printf("------------"));
        out.printf("--------------"); // AVG Nodes/S
        out.printf("\n");
    }

    private void printMovesPerLevelStatics(AtomicInteger maxLevelVisited, List<SessionReportModel> reportRows) {
        out.println("\n Max distinct moves per search level");

        // Marco superior de la tabla
        out.printf(" ___________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("___________"));
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s", row.engineName);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("| %8d ", row.maxDistinctMovesPerLevel[depth]));
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" -----------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("-----------"));
        out.printf("\n");
    }


    private void printCutoffStatics(AtomicInteger maxLevelVisited, List<SessionReportModel> reportRows) {
        out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("___________"));
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("| %6d %% ", row.cutoffPercentages[depth]));
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("-----------"));
        out.printf("\n");
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
