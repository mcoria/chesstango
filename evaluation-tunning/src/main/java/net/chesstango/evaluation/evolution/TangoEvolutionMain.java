package net.chesstango.evaluation.evolution;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorBasic;
import net.chesstango.evaluation.imp.GameEvaluatorImp01;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.arena.Match;
import net.chesstango.uci.arena.MathResult;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class TangoEvolutionMain {

    private static final List<String> GAMES = Arrays.asList(FENDecoder.INITIAL_FEN,
            "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20",
            "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7",
            "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5");

    public static void main(String[] args) {
        EngineController engine1 = createController(GameEvaluatorBasic.class);

        EngineController engine2 = createController(GameEvaluatorImp02.class);

        Instant start = Instant.now();

        Match match = new Match(engine1, engine2, 1);

        startEngines(engine1, engine2);

        List<MathResult> matchResult = match.play(GAMES);

        quitEngines(engine1, engine2);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time elapsed: " + timeElapsed.toMillis() + " ms");

        long puntosAsWhite = matchResult.stream().filter(result -> result.getEngineWhite() == engine1).mapToLong(result -> result.getPoints()).sum();
        long puntosAsBlack = (-1) * matchResult.stream().filter(result -> result.getEngineBlack() == engine1).mapToLong(result -> result.getPoints()).sum();
        long puntosTotal = puntosAsWhite + puntosAsBlack;

        System.out.println("Puntos withe = " + puntosAsWhite + ", puntos black = " + puntosAsBlack + ", total = " + puntosTotal);
    }

    private static EngineController createController(Class<? extends GameEvaluator> gameEvaluatorClass) {
        SearchMove search = new DefaultSearchMove();

        try {
            search.setGameEvaluator(gameEvaluatorClass.getDeclaredConstructor().newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        EngineControllerImp controller = new EngineControllerImp(new EngineTango(search));
        controller.overrideEngineName(gameEvaluatorClass.getSimpleName());
        return controller;
    }

    public static void startEngines(EngineController engine1, EngineController engine2) {
        engine1.send_CmdUci();
        engine1.send_CmdIsReady();

        engine2.send_CmdUci();
        engine2.send_CmdIsReady();
    }

    public static void quitEngines(EngineController engine1, EngineController engine2) {
        engine1.send_CmdQuit();
        engine2.send_CmdQuit();
    }

}
