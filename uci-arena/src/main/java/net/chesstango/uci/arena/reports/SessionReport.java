package net.chesstango.uci.arena.reports;

import net.chesstango.board.moves.Move;
import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public void printTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {

        List<ReportRowModel> reportRows = new ArrayList<>();

        enginesOrder.forEach(engineController -> {
            List<Session> sessionsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(GameResult::getSessionWhite).collect(Collectors.toList());
            List<Session> sessionsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(GameResult::getSessionBlack).collect(Collectors.toList());

            if (breakByColor) {
                if (sessionsWhite.size() > 0) {
                    reportRows.add(collectStatics(String.format("%s white", engineController.getEngineName()), sessionsWhite));
                }
                if (sessionsBlack.size() > 0) {
                    reportRows.add(collectStatics(String.format("%s black", engineController.getEngineName()), sessionsBlack));
                }
            } else {
                List<Session> sessions = new ArrayList<>();
                sessions.addAll(sessionsWhite);
                sessions.addAll(sessionsBlack);
                if (sessions.size() > 0) {
                    reportRows.add(collectStatics(engineController.getEngineName(), sessions));
                }
            }
        });

        print(reportRows);
    }

    private ReportRowModel collectStatics(String engineName, List<Session> sessions) {
        ReportRowModel rowModel = new ReportRowModel();
        rowModel.engineName = engineName;

        rowModel.searches = sessions.stream().map(Session::getSearches).flatMap(List::stream).count();
        rowModel.searchesWithoutCollisions = sessions.stream().map(Session::getSearches).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value == 0).count();
        rowModel.searchesWithoutCollisionsPercentage = (int) ((rowModel.searchesWithoutCollisions * 100) / rowModel.searches);
        rowModel.searchesWithCollisions = sessions.stream().map(Session::getSearches).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).count();
        rowModel.searchesWithCollisionsPercentage = (int) ((rowModel.searchesWithCollisions * 100) / rowModel.searches);
        if (rowModel.searchesWithCollisions > 0) {
            rowModel.avgOptionsPerCollision = sessions.stream().map(Session::getSearches).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).average().getAsDouble();
        }


        long[] expectedNodesCounters = new long[30];
        rowModel.visitedNodesCounters = new long[30];
        rowModel.visitedNodesCountersAvg = new int[30];
        rowModel.visitedNodesTotal = 0;
        rowModel.cutoffPercentages = new int[30];
        rowModel.maxDistinctMovesPerLevel = new int[30];
        rowModel.maxLevelVisited = 0;

        sessions.stream().map(Session::getSearches).flatMap(List::stream).forEach(searchMoveResult -> {
            int maxLevel = 0;
            int[] currentNodeCounters = searchMoveResult.getVisitedNodesCounters();
            int[] currentExpectedNodeCounters = searchMoveResult.getExpectedNodesCounters();
            for (int i = 0; i < currentNodeCounters.length; i++) {
                rowModel.visitedNodesCounters[i] += currentNodeCounters[i];
                expectedNodesCounters[i] += currentExpectedNodeCounters[i];
                if (currentNodeCounters[i] > 0) {
                    maxLevel = i + 1;
                }
            }
            if (rowModel.maxLevelVisited < maxLevel) {
                rowModel.maxLevelVisited = maxLevel;
            }


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
        });

        for (int i = 0; i < 30; i++) {
            rowModel.visitedNodesTotal += rowModel.visitedNodesCounters[i];
            rowModel.visitedNodesCountersAvg[i] = (int) (rowModel.visitedNodesCounters[i] / rowModel.searches);
            if (expectedNodesCounters[i] > 0) {
                rowModel.cutoffPercentages[i] = (int) (100 - (100 * rowModel.visitedNodesCounters[i] / expectedNodesCounters[i]));
            }
        }

        rowModel.visitedNodesTotalAvg = (int) (rowModel.visitedNodesTotal / rowModel.searches);

        return rowModel;
    }

    private void print(List<ReportRowModel> reportRows) {
        AtomicInteger maxLevelVisited = new AtomicInteger();
        for (ReportRowModel reportRowModel : reportRows) {
            if (maxLevelVisited.get() < reportRowModel.maxLevelVisited) {
                maxLevelVisited.set(reportRowModel.maxLevelVisited);
            }
        }


        if (printCollisionStatics) {
            printCollisionStatics(reportRows);
        }

        if (printNodesVisitedStatics) {
            printNodesVisitedStatics(maxLevelVisited, reportRows);
            printNodesVisitedStaticsAvg(maxLevelVisited, reportRows);
        }

        if (printMovesPerLevelStatics) {
            printMovesPerLevelStatics(maxLevelVisited, reportRows);
        }

        if (printCutoffStatics) {
            printCutoffStatics(maxLevelVisited, reportRows);
        }

    }

    private void printCollisionStatics(List<ReportRowModel> reportRows) {
        // Marco superior de la tabla
        System.out.printf(" ___________________________________________________________________________________________________________________________________");
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | wo/COLLISIONS%% | w/COLLISIONS%% | MovesCollision AVG ");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d |%14d |%13d |%12d %%  |%12d %% |%19.1f ", row.engineName, row.searches, row.searchesWithoutCollisions, row.searchesWithCollisions, row.searchesWithoutCollisionsPercentage, row.searchesWithCollisionsPercentage, row.avgOptionsPerCollision);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" -----------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("\n");
    }

    private void printNodesVisitedStatics(AtomicInteger maxLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Nodes visited per search level");

        // Marco superior de la tabla
        System.out.printf(" ______________________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        System.out.printf("______________"); // Nodes
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("| Total Nodes ");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %8d ", row.visitedNodesCounters[depth]));
            System.out.printf("| %11d ", row.visitedNodesTotal);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" ----------------------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("-----------"));
        System.out.printf("--------------"); // Total Nodes
        System.out.printf("\n");
    }

    private void printNodesVisitedStaticsAvg(AtomicInteger maxLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Nodes visited per search level AVG");

        // Marco superior de la tabla
        System.out.printf(" ______________________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        System.out.printf("______________"); // AVG Nodes/S
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("| AVG Nodes/S ");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %8d ", row.visitedNodesCountersAvg[depth]));
            System.out.printf("| %11d ", row.visitedNodesTotalAvg);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" ----------------------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("-----------"));
        System.out.printf("--------------"); // AVG Nodes/S
        System.out.printf("\n");
    }

    private void printMovesPerLevelStatics(AtomicInteger maxLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Max distinct moves per search level");

        // Marco superior de la tabla
        System.out.printf(" ___________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s", row.engineName);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %8d ", row.maxDistinctMovesPerLevel[depth]));
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" -----------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("-----------"));
        System.out.printf("\n");
    }


    private void printCutoffStatics(AtomicInteger maxLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        System.out.printf(" ___________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %6d %% ", row.cutoffPercentages[depth]));
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" -----------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("-----------"));
        System.out.printf("\n");
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

    private static class ReportRowModel {
        String engineName;
        long searches;
        int maxLevelVisited;

        ///////////////////// START COLLISIONS
        long searchesWithoutCollisions;
        int searchesWithoutCollisionsPercentage;
        long searchesWithCollisions;
        int searchesWithCollisionsPercentage;
        double avgOptionsPerCollision;
        ///////////////////// END COLLISIONS

        ///////////////////// START VISITED NODES
        long[] visitedNodesCounters;
        int[] visitedNodesCountersAvg;
        int[] cutoffPercentages;
        long visitedNodesTotal;
        int visitedNodesTotalAvg;
        ///////////////////// END VISITED NODES

        int[] maxDistinctMovesPerLevel;
    }
}
