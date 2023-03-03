package net.chesstango.evaluation.evolution;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorBasic;
import net.chesstango.evaluation.imp.GameEvaluatorImp01;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.MatchMain;
import net.chesstango.uci.arena.Tournament;
import net.chesstango.uci.arena.reports.Reports;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class TangoEvolutionMain {

    public static void main(String[] args) {
        EngineController engineBasic = createController(GameEvaluatorBasic.class);
        EngineController engine1 = createController(GameEvaluatorImp01.class);
        EngineController engine2 = createController(GameEvaluatorImp02.class);

        List<EngineController> allEngines = Arrays.asList(engineBasic, engine1, engine2);

        Instant start = Instant.now();

        Tournament tournament = new Tournament(engine2, Arrays.asList(engineBasic, engine1));

        allEngines.forEach(TangoEvolutionMain::startEngine);

        List<GameResult> matchResult = tournament.play(MatchMain.GAMES_BALSA_TOP10);

        allEngines.forEach(TangoEvolutionMain::quitEngine);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time elapsed: " + timeElapsed.toMillis() + " ms");

        new Reports().printByEngine(engine2, matchResult);
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

    public static void startEngine(EngineController engine) {
        engine.send_CmdUci();
        engine.send_CmdIsReady();
    }

    public static void quitEngine(EngineController engine) {
        engine.send_CmdQuit();
    }

}
