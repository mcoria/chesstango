package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.Transcoding;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.*;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.arena.reports.GameReports;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mauricio Coria
 */
public class TournamentMain {

    private static final int DEPTH = 2;

    public static void main(String[] args) {
        List<EngineControllerFactory> controllerFactories = createControllerFactories();

        List<GameResult> matchResult = Collections.synchronizedList( new ArrayList<>() );
        MatchListener matchListener = new MatchListener() {
            @Override
            public void notifyNewGame(Game game, EngineController white, EngineController black) {}

            @Override
            public void notifyMove(Game game, Move move) {}

            @Override
            public void notifyEndGame(GameResult gameResult) {
                matchResult.add(gameResult);
            }
        };

        Tournament tournament = new Tournament(controllerFactories, DEPTH, matchListener);
        Instant start = Instant.now();
        tournament.play(getFenList());
        System.out.println("Time elapsed: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        List<List<EngineController>> allControllerFactories = new ArrayList<>();
        allControllerFactories.addAll(controllerFactories.stream().map(EngineControllerFactory::getCreatedEngineControllers).collect(Collectors.toList()));

        new GameReports().printReport(allControllerFactories, matchResult);
    }

    private static List<String> getFenList(){
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(TournamentMain.class.getClassLoader().getResourceAsStream("Balsa_v2724.pgn"));
        List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        return fenList;
    }


    private static List<EngineControllerFactory> createControllerFactories() {
        EngineControllerFactory mainFactory = new EngineControllerFactory(() -> EngineControllerFactory.createTangoController(GameEvaluatorSEandImp02.class));
        EngineControllerFactory factory1 = new EngineControllerFactory(() -> EngineControllerFactory.createTangoController(GameEvaluatorByMaterial.class));
        EngineControllerFactory factory2 = new EngineControllerFactory(() -> EngineControllerFactory.createTangoController(GameEvaluatorByMaterialAndMoves.class));
        EngineControllerFactory factory3 = new EngineControllerFactory(() -> EngineControllerFactory.createTangoController(GameEvaluatorImp01.class));
        EngineControllerFactory factory4 = new EngineControllerFactory(() -> EngineControllerFactory.createTangoController(GameEvaluatorImp02.class));
        EngineControllerFactory factory5 = new EngineControllerFactory(() -> EngineControllerFactory.createTangoController(GameEvaluatorSimplifiedEvaluator.class));

        EngineControllerFactory spikeFactory = new EngineControllerFactory(() -> EngineControllerFactory.createProxyController("Spike", null) );

        return Arrays.asList(mainFactory, factory1, factory2, factory4, factory3, factory5, spikeFactory);
    }

}
