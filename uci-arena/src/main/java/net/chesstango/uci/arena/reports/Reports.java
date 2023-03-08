package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class Reports {

    private class ReportRowModel {
        public String engineName;
        public long wonAsWhite;
        public long wonAsBlack;

        public long lostAsWhite;
        public long lostAsBlack;

        public long drawsAsWhite;
        public long drawsAsBlack;
        public double puntosAsWhite;
        public double puntosAsBlack;
        public double puntosTotal;
        public long playedGames;
        public double winPercentage;
    }

    public void printReport(List<List<EngineController>> controllersListCollection, List<GameResult> matchResult) {
        List<ReportRowModel> rows = new ArrayList<>();
        controllersListCollection.forEach(controllerList -> rows.add(createRowModelMultipleEngine(controllerList, matchResult)));
        printReport(rows);
    }


    public void printEngineControllersReport(List<EngineController> engines, List<GameResult> matchResult) {
        List<ReportRowModel> rows = new ArrayList<>();
        engines.forEach(engine -> rows.add(createRowModelSingleEngine(engine, matchResult)));
        printReport(rows);
    }

    private ReportRowModel createRowModelMultipleEngine(List<EngineController> mainControllers, List<GameResult> matchResult) {
        ReportRowModel row = new ReportRowModel();

        String engineName = "UNKNOWN";
        Set<String> engineNames = mainControllers.stream().map(EngineController::getEngineName).collect(Collectors.toSet());
        if(engineNames.size() == 1){
            engineName =  engineNames.stream().findAny().get();
        }
        row.engineName = engineName;

        row.wonAsWhite = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineWhite()) && result.getWinner() == result.getEngineWhite()).count();
        row.lostAsWhite = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineWhite()) && result.getWinner() == result.getEngineBlack()).count();
        row.drawsAsWhite = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineWhite()) && result.getWinner() == null).count();
        row.puntosAsWhite = row.wonAsWhite + 0.5 * row.drawsAsWhite;

        row.wonAsBlack = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineBlack()) && result.getWinner() == result.getEngineBlack()).count();
        row.lostAsBlack = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineBlack()) && result.getWinner() == result.getEngineWhite()).count();
        row.drawsAsBlack = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineBlack()) && result.getWinner() == null).count();
        row.puntosAsBlack = row.wonAsBlack + 0.5 * row.drawsAsBlack;

        row.puntosTotal = row.puntosAsWhite + row.puntosAsBlack;
        row.playedGames = matchResult.stream().filter(result -> mainControllers.contains(result.getEngineWhite()) || mainControllers.contains(result.getEngineBlack())).count();

        row.winPercentage = (row.puntosTotal / row.playedGames) * 100;

        return row;
    }

    protected ReportRowModel createRowModelSingleEngine(EngineController engine, List<GameResult> matchResult) {
        ReportRowModel row = new ReportRowModel();
        row.engineName = engine.getEngineName();
        row.wonAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineWhite()).count();
        row.lostAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineBlack()).count();
        row.drawsAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == null).count();
        row.puntosAsWhite = row.wonAsWhite + 0.5 * row.drawsAsWhite;

        row.wonAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == result.getEngineBlack()).count();
        row.lostAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == result.getEngineWhite()).count();
        row.drawsAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == null).count();
        row.puntosAsBlack = row.wonAsBlack + 0.5 * row.drawsAsBlack;

        row.puntosTotal = row.puntosAsWhite + row.puntosAsBlack;
        row.playedGames = matchResult.stream().filter(result -> result.getEngineWhite() == engine || result.getEngineBlack() == engine).count();

        row.winPercentage = (row.puntosTotal / row.playedGames) * 100;

        return row;
    }


    private void printReport(List<ReportRowModel> reportRows) {
        System.out.printf(" ___________________________________________________________________________________________________________________________________________________\n");
        System.out.printf("|ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %%|\n");
        reportRows.forEach(row -> {
            System.out.printf("|%35s|%8d |%8d |%9d |%9d |%9d |%9d |%11.1f |%11.1f |%6.1f /%3d | %6.1f |\n", row.engineName, row.wonAsWhite, row.wonAsBlack, row.lostAsWhite, row.lostAsBlack, row.drawsAsWhite, row.drawsAsBlack, row.puntosAsWhite, row.puntosAsBlack, row.puntosTotal, row.playedGames, row.winPercentage);
        });
        System.out.printf(" ---------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }


    public void printTangoStatics(List<Session> sessions, boolean printDetail) {
        class EvaluationStatics {
            String fen;
            long noCollisionCount;
            long collisionCount;
            int sum;
        }

        List<EvaluationStatics> staticsPerSession = new ArrayList<>();
        sessions.forEach(session -> {
            EvaluationStatics statics = new EvaluationStatics();
            statics.fen = session.getInitialPosition();
            statics.noCollisionCount = session.getMoveResultList().stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value == 0).count();
            statics.collisionCount = session.getMoveResultList().stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).count();
            statics.sum = session.getMoveResultList().stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).sum();
            staticsPerSession.add(statics);
        });

        long noCollisionCount = staticsPerSession.stream().mapToLong(es -> es.noCollisionCount).sum();
        long collisionCount = staticsPerSession.stream().mapToLong(es -> es.collisionCount).sum();
        int sum = staticsPerSession.stream().mapToInt(es -> es.sum).sum();

        System.out.printf("Summary collisions noCollisionCount=%d collisionCount=%d sum=%d \n", noCollisionCount, collisionCount, sum);

        if (printDetail) {
            for (EvaluationStatics statics : staticsPerSession) {
                String fen = statics.fen;
                noCollisionCount = statics.noCollisionCount;
                collisionCount = statics.collisionCount;
                sum = statics.sum;
                System.out.printf("%s collisions noCollisionCount=%d collisionCount=%d sum=%d \n", fen, noCollisionCount, collisionCount, sum);
            }
        }
    }

}

