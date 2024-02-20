package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.Transcoding;
import net.chesstango.evaluation.evaluators.*;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerMbeans;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.reports.SummaryReport;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TournamentMain implements MatchListener {

    private static final MatchByDepth matchType = new MatchByDepth(2);

    public static void main(String[] args) {
        List<EngineControllerPoolFactory> controllerFactories = createControllerFactories();

        List<MatchResult> matchResult = new TournamentMain(controllerFactories).play(getFenList());

        List<List<EngineController>> allControllerFactories = controllerFactories.stream().map(EngineControllerPoolFactory::getEngineControllerInstances).toList();

        new SummaryReport()
                .withMultipleEngineInstances(allControllerFactories, matchResult)
                .printReport(System.out);
    }

    private static List<String> getFenList() {
        List<String> fenList = new Transcoding().pgnFileToFenPositions(TournamentMain.class.getClassLoader().getResourceAsStream("Balsa_v2724.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        return fenList;
    }


    private static List<EngineControllerPoolFactory> createControllerFactories() {
        EngineControllerPoolFactory mainFactory = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorSEandImp02.class));
        EngineControllerPoolFactory factory1 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterial.class));
        EngineControllerPoolFactory factory2 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterialAndMoves.class));
        EngineControllerPoolFactory factory3 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp01.class));
        EngineControllerPoolFactory factory4 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp02.class));
        EngineControllerPoolFactory factory5 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorSimplifiedEvaluator.class));

        EngineControllerPoolFactory spikeFactory = new EngineControllerPoolFactory(() -> EngineControllerFactory.createProxyController("Spike", null));

        return Arrays.asList(mainFactory, factory1, factory2, factory4, factory3, factory5, spikeFactory);
    }

    private final List<EngineControllerPoolFactory> controllerFactories;
    private final List<MatchResult> matchResult;

    public TournamentMain(List<EngineControllerPoolFactory> controllerFactories) {
        this.controllerFactories = controllerFactories;
        this.matchResult = Collections.synchronizedList(new ArrayList<>());
    }

    public List<MatchResult> play(List<String> fenList) {
        MatchBroadcaster matchBroadcaster = new MatchBroadcaster();
        matchBroadcaster.addListener(new MatchListenerMbeans());
        matchBroadcaster.addListener(this);

        Tournament tournament = new Tournament(controllerFactories, matchType, matchBroadcaster);

        Instant start = Instant.now();
        tournament.play(fenList);
        System.out.println("Time elapsed: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        return matchResult;
    }

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
    }

    @Override
    public void notifyMove(Game game, Move move) {
    }

    @Override
    public void notifyEndGame(Game game, MatchResult matchResult) {
        this.matchResult.add(matchResult);
        matchResult.save();
    }
}
