package net.chesstango.uci.arena;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp02;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.CaptureMatchResult;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBeans;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.reports.SummaryReport;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TournamentMain {

    private static final MatchByDepth matchType = new MatchByDepth(2);

    public static void main(String[] args) {
        List<EngineControllerPoolFactory> controllerFactories = createControllerFactories();

        List<MatchResult> matchResult = new TournamentMain(controllerFactories)
                .play(getFenList());

        List<List<EngineController>> allControllerFactories = controllerFactories.stream()
                .map(EngineControllerPoolFactory::getEngineControllerInstances)
                .toList();

        new SummaryReport()
                .withMultipleEngineInstances(allControllerFactories, matchResult)
                .printReport(System.out);
    }

    private static List<String> getFenList() {
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(TournamentMain.class.getClassLoader().getResourceAsStream("Balsa_v2724.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        List<String> fenList = List.of(FENDecoder.INITIAL_FEN);
        return fenList;
    }


    private static List<EngineControllerPoolFactory> createControllerFactories() {
        EngineControllerPoolFactory mainFactory = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorSEandImp02.class));
        /*
        EngineControllerPoolFactory factory1 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterial.class));
        EngineControllerPoolFactory factory2 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterialAndMoves.class));
        EngineControllerPoolFactory factory3 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp01.class));
        EngineControllerPoolFactory factory4 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp02.class));
        EngineControllerPoolFactory factory5 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorSimplifiedEvaluator.class));
         */
        EngineControllerPoolFactory spikeFactory = new EngineControllerPoolFactory(() -> EngineControllerFactory.createProxyController("Spike", null));

        return Arrays.asList(mainFactory, spikeFactory);
    }

    private final List<EngineControllerPoolFactory> controllerFactories;

    public TournamentMain(List<EngineControllerPoolFactory> controllerFactories) {
        this.controllerFactories = controllerFactories;
    }

    public List<MatchResult> play(List<String> fenList) {
        CaptureMatchResult captureMatchResult = new CaptureMatchResult();

        Tournament tournament = new Tournament(controllerFactories, matchType, new MatchBroadcaster()
                .addListener(new MatchListenerToMBeans())
                .addListener(new SavePGNGame())
                .addListener(captureMatchResult));

        Instant start = Instant.now();
        tournament.play(fenList);
        System.out.println("Time elapsed: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        return captureMatchResult.getMatchResults();
    }
}
