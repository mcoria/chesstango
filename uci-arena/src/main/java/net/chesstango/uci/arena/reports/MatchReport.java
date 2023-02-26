package net.chesstango.uci.arena.reports;

import net.chesstango.uci.arena.MathResult;
import net.chesstango.uci.gui.EngineController;

import java.util.List;

public class MatchReport {

    public void printByEngine(EngineController engine1, EngineController engine2, List<MathResult> matchResult) {
        printByEngine(engine1, matchResult);
        printByEngine(engine2, matchResult);
    }

    private void printByEngine(EngineController engine, List<MathResult> matchResult) {
        long puntosAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine).mapToLong(result -> result.getPoints()).sum();
        long wonAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine && result.getWinner() == engine).count();
        long puntosAsBlack = (-1) * matchResult.stream().filter(result -> result.getEngineBlack() == engine).mapToLong(result -> result.getPoints()).sum();
        long wonAsBlack = matchResult.stream().filter(result -> result.getEngineBlack() == engine && result.getWinner() == engine).count();
        long puntosTotal = puntosAsWhite + puntosAsBlack;

        System.out.printf("%s as white = %d (%d won), as black = %d (%d won), total = %d\n", engine.getEngineName(), puntosAsWhite, wonAsWhite, puntosAsBlack, wonAsBlack, puntosTotal);
    }
}
