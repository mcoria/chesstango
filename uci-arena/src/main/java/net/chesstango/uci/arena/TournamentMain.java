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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class TournamentMain {

    public static void main(String[] args) {
        List<EngineControllerFactory> controllerFactories = new ArrayList<>();
        controllerFactories.add(new EngineControllerFactory(() -> createTangoController(GameEvaluatorImp02.class)));
        controllerFactories.addAll(createOpponentsControllerFactories());

        Tournament tournament = new Tournament(controllerFactories);
        Instant start = Instant.now();
        List<GameResult> matchResult = tournament.play(new Transcoding().pgnFileToFenPositions(TournamentMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn")));
        System.out.println("Time elapsed: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        List<List<EngineController>> allControllerFactories = new ArrayList<>();
        allControllerFactories.addAll(controllerFactories.stream().map(EngineControllerFactory::getEngineControllers).collect(Collectors.toList()));

        new Reports().printReport(allControllerFactories, matchResult);
    }

    private static List<EngineControllerFactory> createOpponentsControllerFactories() {
        EngineControllerFactory factory1 = new EngineControllerFactory(() -> createTangoController(GameEvaluatorByMaterial.class));
        EngineControllerFactory factory2 = new EngineControllerFactory(() -> createTangoController(GameEvaluatorByMaterialAndMoves.class));
        EngineControllerFactory factory3 = new EngineControllerFactory(() -> createTangoController(GameEvaluatorImp01.class));

        EngineControllerFactory spikeFactory = new EngineControllerFactory(() -> new EngineControllerImp(new EngineProxy(ProxyConfig.loadEngineConfig("Spike"))));

        return Arrays.asList(factory1, factory2, factory3, spikeFactory);
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

}
