package net.chesstango.uci.arena;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
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
        //EngineControllerFactory factory = new EngineControllerFactory(()->new EngineProxy(ProxyConfig.loadEngineConfig("MORA")));

        EngineControllerFactory factory = new EngineControllerFactory(() -> createTangoController(GameEvaluatorImp02.class));

        Tournament tournament = new Tournament(factory, opponents);

        opponents.forEach(TournamentMain::startEngine);

        Instant start = Instant.now();

        List<GameResult> matchResult = tournament.play(new Transcoding().pgnFileToFenPositions(TournamentMain.class.getClassLoader().getResourceAsStream("Balsa_v500.pgn")));

        Duration timeElapsed = Duration.between(start, Instant.now());
        System.out.println("Time elapsed: " + timeElapsed.toMillis() + " ms");

        opponents.forEach(TournamentMain::quitEngine);


        new Reports().printReport(factory.getEngineControllers(), opponents, matchResult);
    }

    private static List<EngineController> createOpponents() {
        EngineController engine0 = createTangoController(GameEvaluatorByMaterial.class);
        EngineController engine1 = createTangoController(GameEvaluatorByMaterialAndMoves.class);
        EngineController engine2 = createTangoController(GameEvaluatorImp01.class);

        EngineController spike = new EngineControllerImp(new EngineProxy(ProxyConfig.loadEngineConfig("Spike")));
        return Arrays.asList(engine0, engine1, engine2, spike);
    }

    private static EngineController createTangoController(Class<? extends GameEvaluator> gameEvaluatorClass) {
        EngineTango tango = createEngineTango(gameEvaluatorClass);
        EngineControllerImp controller = new EngineControllerImp(tango);
        controller.overrideEngineName(gameEvaluatorClass.getSimpleName());
        return controller;
    }

    private static EngineTango createEngineTango(Class<? extends GameEvaluator> gameEvaluatorClass) {
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
        return new EngineTango(search);
    }

    private static void startEngine(EngineController engine) {
        engine.startEngine();
    }

    private static void quitEngine(EngineController engine) {
        engine.send_CmdQuit();
    }

}
