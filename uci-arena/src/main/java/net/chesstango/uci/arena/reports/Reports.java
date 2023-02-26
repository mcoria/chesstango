package net.chesstango.uci.arena.reports;

import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.gui.EngineController;

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
}
