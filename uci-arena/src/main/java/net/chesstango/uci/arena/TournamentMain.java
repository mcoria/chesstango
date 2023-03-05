package net.chesstango.uci.arena;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterialAndMoves;
import net.chesstango.evaluation.imp.GameEvaluatorImp01;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.arena.reports.Reports;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TournamentMain {

    public static void main(String[] args) {
        List<EngineController> opponents = createOpponents();

        EngineControllerFactory factory = new EngineControllerFactory(()->new EngineProxy(ProxyConfig.loadEngineConfig("MORA")));

        Tournament tournament = new Tournament(factory, opponents);

        opponents.forEach(TournamentMain::startEngine);

        Instant start = Instant.now();
        List<GameResult> matchResult = tournament.play(MatchMain.GAMES_BALSA_TOP10);
        Duration timeElapsed = Duration.between(start, Instant.now());

        opponents.forEach(TournamentMain::quitEngine);

        System.out.println("Time elapsed: " + timeElapsed.toMillis() + " ms");

        List<EngineController> mainControllers = factory.getEngineControllers();
        
        new Reports().printReport(mainControllers, opponents, matchResult);
    }

    private static List<EngineController> createOpponents() {
        EngineController engineBasic = createTangoController(GameEvaluatorByMaterialAndMoves.class);
        EngineController engine1 = createTangoController(GameEvaluatorImp01.class);
        EngineController engine2 = createTangoController(GameEvaluatorImp02.class);
        return Arrays.asList(engineBasic, engine1, engine2);
    }

    private static EngineController createTangoController(Class<? extends GameEvaluator> gameEvaluatorClass) {
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

    private static void startEngine(EngineController engine) {
        engine.startEngine();
    }

    private static void quitEngine(EngineController engine) {
        engine.send_CmdQuit();
    }

}
