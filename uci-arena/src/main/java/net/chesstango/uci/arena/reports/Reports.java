package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Reports {

    public void printByEngine(EngineController engine1, EngineController engine2, List<GameResult> matchResult) {

        System.out.printf(" __________________________________________________________________________________________________________________\n");
        System.out.printf("|ENGINE NAME|WHITE WON|WHITE LOST|WHITE DRAW|BLACK WON|BLACK LOST|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|\n");
        printByEngine(engine1, matchResult);
        printByEngine(engine2, matchResult);
        System.out.printf(" ------------------------------------------------------------------------------------------------------------------\n");
    }

    public void printByEngine(EngineController engine, List<GameResult> matchResult) {
        long wonAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineWhite()).count();
        long lostAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineBlack()).count();
        long drawsAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == null).count();
        double puntosAsWhite = wonAsWhite + 0.5 * drawsAsWhite;

        long wonAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == result.getEngineBlack()).count();
        long lostAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == result.getEngineWhite()).count();
        long drawsAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == null).count();
        double puntosAsBlack = wonAsBlack + 0.5 * drawsAsBlack;

        long playedGames = matchResult.stream().filter(result -> result.getEngineWhite() == engine || result.getEngineBlack() == engine).count();

        double puntosTotal = puntosAsWhite + puntosAsBlack;

        System.out.printf("|%11s|%9d|%10d|%10d|%9d|%10d|%10d|%12.1f|%12.1f|%6.1f /%3d |\n", engine.getEngineName(), wonAsWhite, lostAsWhite, drawsAsWhite, wonAsBlack, lostAsBlack, drawsAsBlack, puntosAsWhite, puntosAsBlack, puntosTotal, playedGames);

        /*
        System.out.println("Won as White:");
        printTangoStatics(matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineWhite()).map(GameResult::getSessionWhite).filter(Objects::nonNull).collect(Collectors.toList()), true);

        System.out.println("Lost as White:");
        printTangoStatics(matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineBlack()).map(GameResult::getSessionWhite).filter(Objects::nonNull).collect(Collectors.toList()), true);
         */
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

