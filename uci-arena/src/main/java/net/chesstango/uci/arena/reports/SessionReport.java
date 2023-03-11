package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SessionReport {

    public void printTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {

        List<ReportRowModel> reportRows = new ArrayList<>();

        enginesOrder.forEach(engineController -> {
            List<Session> sessions = new ArrayList<>();
            sessions.addAll(matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(GameResult::getSessionWhite).collect(Collectors.toList()));
            sessions.addAll(matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(GameResult::getSessionBlack).collect(Collectors.toList()));

            reportRows.add(collectStatics(engineController, sessions));

        });

        print(reportRows);
    }

    private ReportRowModel collectStatics(EngineController engineController, List<Session> sessions) {
        ReportRowModel rowModel = new ReportRowModel();
        rowModel.engineName = engineController.getEngineName();

        rowModel.searches = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).count();
        rowModel.searchesWithoutCollisions = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value == 0).count();
        rowModel.searchesWithCollisions = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).count();
        rowModel.collisions = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).sum();

        rowModel.visitedNodeCounters = new int[20];
        rowModel.totalVisitedNodes = 0;
        sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).forEach(searchMoveResult -> {
            int[] currentNodeCounters = searchMoveResult.getVisitedNodesCounter();
            for (int i = 0; i < 20 && i < currentNodeCounters.length ; i++) {
                rowModel.visitedNodeCounters[i] += currentNodeCounters[i];
                rowModel.totalVisitedNodes += currentNodeCounters[i];
            }
        });

        rowModel.avgNodesPerSearch = rowModel.totalVisitedNodes / (int) rowModel.searches;

        return rowModel;
    }

    private void print(List<ReportRowModel> reportRows) {
        System.out.printf(" __________________________________________________________________________________________\n");
        System.out.printf("|ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS |\n");
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d |%14d |%13d |%11d |\n", row.engineName, row.searches, row.searchesWithoutCollisions, row.searchesWithCollisions, row.collisions);
        });
        System.out.printf(" ------------------------------------------------------------------------------------------\n");

        AtomicInteger maxLevelVisited = new AtomicInteger();
        reportRows.forEach(row -> {
            int maxLevel = 0;

            while (maxLevel < 20 && row.visitedNodeCounters[maxLevel] > 0) {
                maxLevel++;
            }

            if (maxLevelVisited.get() < maxLevel - 1) {
                maxLevelVisited.set(maxLevel - 1);
            }
        });

        System.out.printf(" ___________________________________");
        IntStream.rangeClosed(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("____________"));
        System.out.printf("____________"); // Nodes
        System.out.printf("____________"); // NodesPerSearch
        System.out.printf("\n");

        System.out.printf("|ENGINE NAME                        ");
        IntStream.rangeClosed(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d  ", depth + 1));
        System.out.printf("|Total Nodes");
        System.out.printf("|AVG Nodes/S");
        System.out.printf("|\n");

        reportRows.forEach(row -> {
            System.out.printf("|%35s", row.engineName);
            IntStream.rangeClosed(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %9d ", row.visitedNodeCounters[depth]));
            System.out.printf("| %9d ", row.totalVisitedNodes);
            System.out.printf("| %9d ", row.avgNodesPerSearch);
            System.out.printf("|\n");
        });

        System.out.printf(" -----------------------------------");
        IntStream.rangeClosed(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("------------"));
        System.out.printf("------------"); // Nodes
        System.out.printf("------------"); // NodesPerSearch
        System.out.printf("\n");

    }

    private class ReportRowModel {
        String engineName;

        long searches;

        long searchesWithoutCollisions;

        long searchesWithCollisions;

        int collisions;

        int[] visitedNodeCounters;
        int totalVisitedNodes;

        int avgNodesPerSearch;
    }
}
