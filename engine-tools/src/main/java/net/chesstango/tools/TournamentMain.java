package net.chesstango.tools;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.pgn.PGN;
import net.chesstango.board.representations.pgn.PGNStringDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterialAndPST;
import net.chesstango.evaluation.evaluators.EvaluatorImp02;
import net.chesstango.tools.search.reports.arena.SummaryReport;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.Tournament;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.listeners.CaptureMatchResult;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBeans;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class TournamentMain {

    private static final MatchByDepth matchType = new MatchByDepth(2);

    public static void main(String[] args) {
        Supplier<EngineController> main = () -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterialAndPST::new);
        Supplier<EngineController> evaluatorImp02 = () -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp02::new);
        /*
        EngineControllerPoolFactory factory1 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterial.class));
        EngineControllerPoolFactory factory2 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorByMaterialAndMoves.class));
        EngineControllerPoolFactory factory3 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp01.class));

        EngineControllerPoolFactory factory5 = new EngineControllerPoolFactory(() -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorSimplifiedEvaluator.class));
         */
        Supplier<EngineController> spike = () -> EngineControllerFactory.createProxyController("Spike", null);


        List<Supplier<EngineController>> engineSupplierList = Arrays.asList(main, evaluatorImp02, spike);

        List<MatchResult> matchResult = new TournamentMain(engineSupplierList)
                .play(getFenList());

        new SummaryReport()
                .withMatchResults(matchResult)
                .printReport(System.out);
    }

    private static Stream<FEN> getFenList() {
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(TournamentMain.class.getClassLoader().getResourceAsStream("Balsa_v2724.pgn"));
        Stream<PGN> pgnStream = new PGNStringDecoder().decodePGNs(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        //List<String> fenList = List.of(FENDecoder.INITIAL_FEN);
        return pgnStream.map(PGN::toGame).map(Game::getCurrentFEN);
    }

    private final List<Supplier<EngineController>> engineSupplierList;

    public TournamentMain(List<Supplier<EngineController>> engineSupplierList) {
        this.engineSupplierList = engineSupplierList;
    }

    public List<MatchResult> play(Stream<FEN> fenList) {
        CaptureMatchResult captureMatchResult = new CaptureMatchResult();

        Tournament tournament = new Tournament(engineSupplierList, matchType)
                .setMatchListener(new MatchBroadcaster()
                        .addListener(new MatchListenerToMBeans())
                        .addListener(new SavePGNGame())
                        .addListener(captureMatchResult));

        Instant start = Instant.now();
        tournament.play(fenList);
        System.out.println("Time elapsed: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        return captureMatchResult.getMatchResults();
    }
}
