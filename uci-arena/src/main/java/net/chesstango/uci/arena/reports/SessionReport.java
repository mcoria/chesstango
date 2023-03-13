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
 * @author Mauricio Coria
 */
public class SessionReport {


    private boolean printCollisionStatics;
    private boolean printNodesStatics;


    public void printTangoStatics(List<EngineController> enginesOrder, List<GameResult> matchResult) {

        List<ReportRowModel> reportRows = new ArrayList<>();

        enginesOrder.forEach(engineController -> {
            List<Session> sessions = new ArrayList<>();
            sessions.addAll(matchResult.stream().filter(result -> result.getEngineWhite() == engineController && result.getSessionWhite() != null).map(GameResult::getSessionWhite).collect(Collectors.toList()));
            sessions.addAll(matchResult.stream().filter(result -> result.getEngineBlack() == engineController && result.getSessionBlack() != null).map(GameResult::getSessionBlack).collect(Collectors.toList()));

            if(sessions.size() > 0) {
                reportRows.add(collectStatics(engineController, sessions));
            }

        });

        print(reportRows);
    }

    private ReportRowModel collectStatics(EngineController engineController, List<Session> sessions) {
        ReportRowModel rowModel = new ReportRowModel();
        rowModel.engineName = engineController.getEngineName();

        rowModel.searches = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).count();
        rowModel.searchesWithoutCollisions = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value == 0).count();
        rowModel.searchesWithoutCollisionsPercentage = (int) ((rowModel.searchesWithoutCollisions * 100) / rowModel.searches);
        rowModel.searchesWithCollisions = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).count();
        rowModel.searchesWithCollisionsPercentage = (int) ((rowModel.searchesWithCollisions * 100) / rowModel.searches);
        if(rowModel.searchesWithCollisions > 0) {
            rowModel.avgOptionsPerCollision = sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).average().getAsDouble();
        }


        rowModel.visitedNodeCounters = new long[30];
        rowModel.maxMovesPerLevel = new int[30];
        rowModel.totalVisitedNodes = 0;
        rowModel.maxLevelVisited = 0;
        sessions.stream().map(Session::getMoveResultList).flatMap(List::stream).forEach(searchMoveResult -> {
            int maxLevel = 0;
            int[] currentNodeCounters = searchMoveResult.getVisitedNodesCounter();
            for (int i = 0; i < currentNodeCounters.length ; i++) {
                rowModel.visitedNodeCounters[i] += currentNodeCounters[i];
                rowModel.totalVisitedNodes += currentNodeCounters[i];

                if(currentNodeCounters[i] > 0){
                    maxLevel = i + 1;
                }
            }
            if(rowModel.maxLevelVisited < maxLevel){
                rowModel.maxLevelVisited = maxLevel;
            }

            int level = 0;
            for (Set<Move> moveCollection:
                 searchMoveResult.getDistinctMoves()) {
                if(rowModel.maxMovesPerLevel[level] < moveCollection.size()){
                    rowModel.maxMovesPerLevel[level] = moveCollection.size();
                }
                level++;
            }

        });

        rowModel.avgNodesPerSearch = (int) (rowModel.totalVisitedNodes / rowModel.searches);

        return rowModel;
    }

    private void print(List<ReportRowModel> reportRows) {
        AtomicInteger maxLevelVisited =  new AtomicInteger();
        for (ReportRowModel reportRowModel: reportRows) {
            if(maxLevelVisited.get() < reportRowModel.maxLevelVisited){
                maxLevelVisited.set(reportRowModel.maxLevelVisited);
            }
        }


        if(printCollisionStatics) {
            printCollisionStatics(reportRows);
        }

        if(printNodesStatics){
            printNodesStatics(maxLevelVisited, reportRows);
        }

        //printMovesStatics(maxLevelVisited, reportRows);
    }

    private void printCollisionStatics(List<ReportRowModel> reportRows) {
        // Marco superior de la tabla
        System.out.printf(" __________________________________________________________________________________________________________________________________");
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | wo/COLLISIONS%% | w/COLLISIONS%% | MovesCollision AVG");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d |%14d |%13d |%13d%%  |%13d%% |%18.1f ", row.engineName, row.searches, row.searchesWithoutCollisions, row.searchesWithCollisions, row.searchesWithoutCollisionsPercentage,  row.searchesWithCollisionsPercentage,  row.avgOptionsPerCollision);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" -----------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("\n");
    }

    private void printNodesStatics(AtomicInteger maxLevelVisited, List<ReportRowModel> reportRows) {
        // Marco superior de la tabla
        System.out.printf(" ______________________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("____________"));
        System.out.printf("____________"); // Nodes
        System.out.printf("____________"); // NodesPerSearch
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d  ", depth + 1));
        System.out.printf("|Total Nodes");
        System.out.printf("|AVG Nodes/S");
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %9d ", row.visitedNodeCounters[depth]));
            System.out.printf("| %9d ", row.totalVisitedNodes);
            System.out.printf("| %9d ", row.avgNodesPerSearch);
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" ----------------------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("------------"));
        System.out.printf("------------"); // Nodes
        System.out.printf("------------"); // NodesPerSearch
        System.out.printf("\n");
    }

    private void printMovesStatics(AtomicInteger maxLevelVisited, List<ReportRowModel> reportRows) {
        System.out.println("\n Max distinct moves per search level");

        // Marco superior de la tabla
        System.out.printf(" ___________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("____________"));
        System.out.printf("\n");


        // Nombre de las columnas
        System.out.printf("|ENGINE NAME                        ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| Level %2d  ", depth + 1));
        System.out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            System.out.printf("|%35s", row.engineName);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("| %9d ", row.maxMovesPerLevel[depth]));
            System.out.printf("|\n");
        });

        // Marco inferior de la tabla
        System.out.printf(" -----------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> System.out.printf("------------"));
        System.out.printf("\n");
    }

    public SessionReport withCollisionStatics() {
        this.printCollisionStatics = true;
        return this;
    }

    public SessionReport withNodesStatics(){
        this.printNodesStatics = true;
        return this;
    }

    private class ReportRowModel {
        String engineName;

        long searches;

        ///////////////////// START COLLISIONS
        long searchesWithoutCollisions;

        int searchesWithoutCollisionsPercentage;

        long searchesWithCollisions;

        int searchesWithCollisionsPercentage;

        double avgOptionsPerCollision;
        ///////////////////// END COLLISIONS

        long[] visitedNodeCounters;

        int[] maxMovesPerLevel;
        long totalVisitedNodes;

        int maxLevelVisited;
        int avgNodesPerSearch;
    }
}
