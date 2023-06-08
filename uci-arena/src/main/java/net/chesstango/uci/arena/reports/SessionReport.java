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

        print(reportRows);
    }

    private ReportRowModel collectStatics(String engineName, List<SearchMoveResult> searches) {
        ReportRowModel rowModel = new ReportRowModel();
        rowModel.engineName = engineName;

        rowModel.searches = searches.stream().count();
        rowModel.searchesWithoutCollisions = searches.stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value == 0).count();
        rowModel.searchesWithoutCollisionsPercentage = (int) ((rowModel.searchesWithoutCollisions * 100) / rowModel.searches);
        rowModel.searchesWithCollisions = searches.stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).count();
        rowModel.searchesWithCollisionsPercentage = (int) ((rowModel.searchesWithCollisions * 100) / rowModel.searches);

        if (rowModel.searchesWithCollisions > 0) {
            rowModel.avgOptionsPerCollision = searches.stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).average().getAsDouble();
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
            int[] visitedRNodeCounters = searchMoveResult.getVisitedNodesCounters();
            int[] expectedRNodeCounters = searchMoveResult.getExpectedNodesCounters();
            for (int i = 0; i < visitedRNodeCounters.length; i++) {
                rowModel.visitedRNodesCounters[i] += visitedRNodeCounters[i];
                expectedRNodesCounters[i] += expectedRNodeCounters[i];

            }

            int[] visitedQNodesCounters = searchMoveResult.getVisitedNodesQuiescenceCounter();
            for (int i = 0; i < visitedQNodesCounters.length; i++) {
                rowModel.visitedQNodesCounters[i] += visitedQNodesCounters[i];
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

    private void print(List<ReportRowModel> reportRows) {
        AtomicInteger maxRLevelVisited = new AtomicInteger();
        AtomicInteger maxQLevelVisited = new AtomicInteger();

        for (ReportRowModel reportRowModel : reportRows) {
            if (maxRLevelVisited.get() < reportRowModel.maxSearchRLevel) {
                maxRLevelVisited.set(reportRowModel.maxSearchRLevel);
            }
            if (maxQLevelVisited.get() < reportRowModel.maxSearchQLevel) {
                maxQLevelVisited.set(reportRowModel.maxSearchQLevel);
            }
        }


        if (printCollisionStatics) {
            printCollisionStatics(reportRows);
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


    private void printNodesVisitedStaticsByType(List<ReportRowModel> reportRows) {
        System.out.println("\n Nodes visited per type");

        // Marco superior de la tabla
        System.out.printf(" _____________________________________________________________________________________________________________________________________");
        System.out.printf("\n");

        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES |       RNodes |       QNodes |  Total Nodes |  AVG RNodes |  AVG QNodes |   AVG Nodes ");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            System.out.printf("| %12d ", row.visitedRNodesTotal);
            System.out.printf("| %12d ", row.visitedQNodesTotal);
            System.out.printf("| %12d ", row.visitedNodesTotal);
            System.out.printf("| %11d ", row.visitedRNodesAvg);
            System.out.printf("| %11d ", row.visitedQNodesAvg);
            System.out.printf("| %11d ", row.visitedNodesTotalAvg);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" -------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("\n");
    }

    private void printNodesVisitedStatics(AtomicInteger maxRLevelVisited, AtomicInteger maxQLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Nodes visited per search level");

        // Marco superior de la tabla
        System.out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("____________"));
        System.out.printf("______________"); // Nodes
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("| Total Nodes ");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("| %8d ", row.visitedRNodesCounters[depth]));
            IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("| %9d ", row.visitedQNodesCounters[depth]));
            System.out.printf("| %11d ", row.visitedNodesTotal);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("------------"));
        System.out.printf("--------------"); // Total Nodes
        System.out.printf("\n");
    }

    private void printNodesVisitedStaticsAvg(AtomicInteger maxRLevelVisited, AtomicInteger maxQLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Nodes visited per search level AVG");

        // Marco superior de la tabla
        System.out.printf(" ______________________________________________");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("____________"));
        System.out.printf("______________"); // AVG Nodes/S
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("| QLevel %2d ", depth + 1));
        System.out.printf("| AVG Nodes/S ");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("| %8d ", row.visitedRNodesCountersAvg[depth]));
            IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("| %9d ", row.visitedQNodesCountersAvg[depth]));
            System.out.printf("| %11d ", row.visitedNodesTotalAvg);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" ----------------------------------------------");
        IntStream.range(0, maxRLevelVisited.get()).forEach(depth -> System.out.printf("-----------"));
        IntStream.range(0, maxQLevelVisited.get()).forEach(depth -> System.out.printf("------------"));
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
        System.out.printf(" ______________________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("___________"));
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %6d %% ", row.cutoffPercentages[depth]));
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" ----------------------------------------------");
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

        ///////////////////// START COLLISIONS
        long searchesWithoutCollisions;
        int searchesWithoutCollisionsPercentage;
        long searchesWithCollisions;
        int searchesWithCollisionsPercentage;
        double avgOptionsPerCollision;
        ///////////////////// END COLLISIONS

        ///////// START TOTALS
        long visitedNodesTotal;
        int visitedNodesTotalAvg;
        long visitedRNodesTotal;
        long visitedQNodesTotal;

        int visitedRNodesAvg;
        int visitedQNodesAvg;
        //////// END TOTALS


        ///////////////////// START VISITED REGULAR NODES
        int maxSearchRLevel;
        long[] visitedRNodesCounters;
        int[] visitedRNodesCountersAvg;
        int[] cutoffPercentages;
        ///////////////////// END VISITED REGULAR NODES

        ///////////////////// START VISITED QUIESCENCE NODES
        int maxSearchQLevel;
        int[] visitedQNodesCounters;
        int[] visitedQNodesCountersAvg;
        ///////////////////// END VISITED QUIESCENCE NODES

        int[] maxDistinctMovesPerLevel;
    }
}
