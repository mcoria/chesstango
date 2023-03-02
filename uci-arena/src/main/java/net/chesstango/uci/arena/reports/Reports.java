package net.chesstango.uci.arena.reports;

import net.chesstango.engine.Session;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;

import java.util.ArrayList;
import java.util.List;

public class Reports {

    public void printByEngine(EngineController engine1, EngineController engine2, List<GameResult> matchResult) {
        printByEngine(engine1, matchResult);
        printByEngine(engine2, matchResult);
    }

    public void printByEngine(EngineController engine, List<GameResult> matchResult) {
        long puntosAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine).mapToLong(result -> result.getPoints()).sum();
        long wonAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineWhite()).count();
        long lostAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == result.getEngineBlack()).count();

        long puntosAsBlack = (-1) * matchResult.stream().filter(result -> result.getEngineBlack() == engine).mapToLong(result -> result.getPoints()).sum();
        long wonAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == result.getEngineBlack()).count();
        long lostAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == result.getEngineWhite()).count();

        long puntosTotal = puntosAsWhite + puntosAsBlack;

        System.out.printf("%s as white = %d (%d won, %d lost), as black = %d (%d won, %d lost), total = %d\n", engine.getEngineName(), puntosAsWhite, wonAsWhite, lostAsWhite, puntosAsBlack, wonAsBlack, lostAsBlack, puntosTotal);
    }

    public void printTangoStatics(EngineTango engineTango) {
        List<Session> sessions = engineTango.getSessions();

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
            statics.sum =  session.getMoveResultList().stream().mapToInt(SearchMoveResult::getEvaluationCollisions).filter(value -> value > 0).sum();
            staticsPerSession.add(statics);
        });

        long noCollisionCount = staticsPerSession.stream().mapToLong(es -> es.noCollisionCount).sum();
        long collisionCount = staticsPerSession.stream().mapToLong(es -> es.collisionCount).sum();
        int sum = staticsPerSession.stream().mapToInt(es -> es.sum).sum();

        System.out.printf("Sessions summary collisions noCollisionCount=%d collisionCount=%d sum=%d \n", noCollisionCount, collisionCount, sum);

        for (EvaluationStatics statics : staticsPerSession) {
            String fen = statics.fen;
            noCollisionCount = statics.noCollisionCount;
            collisionCount = statics.collisionCount;
            sum = statics.sum;
            System.out.printf("Sessions %s collisions noCollisionCount=%d collisionCount=%d sum=%d \n", fen, noCollisionCount, collisionCount, sum);
        }
    }
}

