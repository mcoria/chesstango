package net.chesstango.tools;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.pgn.PGN;
import net.chesstango.board.representations.pgn.PGNStringDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorImp05;
import net.chesstango.evaluation.evaluators.EvaluatorImp04;
import net.chesstango.tools.search.reports.arena.SummaryReport;
import net.chesstango.uci.arena.MatchMultiple;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.matchtypes.MatchType;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class MatchMain {
    private static final Logger logger = LoggerFactory.getLogger(MatchMain.class);

    private static final MatchType MATCH_TYPE = new MatchByDepth(3);
    //private static final MatchType MATCH_TYPE = new MatchByTime(200);
    //private static final MatchType MATCH_TYPE = new MatchByClock(1000 * 60 * 3, 1000);

    private static final boolean MATCH_DEBUG = false;
    private static final boolean MATCH_SWITCH_CHAIRS = true;

    /**
     * Add the following JVM parameters:
     * -Dcom.sun.management.jmxremote
     * -Dcom.sun.management.jmxremote.port=19999
     * -Dcom.sun.management.jmxremote.local.only=false
     * -Dcom.sun.management.jmxremote.authenticate=false
     * -Dcom.sun.management.jmxremote.ssl=false
     */
    public static void main(String[] args) {
        Supplier<EngineController> tangoSupplier = () ->
                EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp05::new);
                        /*
                        .createTangoControllerWithDefaultEvaluator(AlphaBetaBuilder.class,
                        builder -> builder
                                .withGameEvaluatorCache()o
                                .withQuiescence()
                                .withTranspositionTable()
                                .withTranspositionMoveSorter()
                                .withAspirationWindows()
                                .withIterativeDeepening()
                                .withStopProcessingCatch()
                                .withStatistics()
                        );*/
        ;

        //Supplier<EngineController> opponentSupplier = () -> EngineControllerFactory.createProxyController("Spike", null);


        Supplier<EngineController> opponentSupplier = () -> EngineControllerFactory.createTangoControllerWithDefaultSearch(EvaluatorImp04::new);


        List<MatchResult> matchResult = new MatchMain(tangoSupplier, opponentSupplier)
                .play();


        // Solo para ordenar la tabla de salida se especifican los engines en la lista

        new SummaryReport()
                .withMatchResults(matchResult)
                //.withMatchResult(List.of(engineController1, engineController2), matchResult)
                .printReport(System.out);

        /*
        new SessionReport()
                .withCollisionStatistics()
                //.withNodesVisitedStatistics()
                //.withCutoffStatistics()
                .breakByColor()
                .withMathResults(List.of(engineController1, engineController2), matchResult)
                .printReport(System.out);


        new SearchesReport()
                //.withCutoffStatistics()
                //.withNodesVisitedStatistics()
                .withPrincipalVariation()
                .withMathResults(List.of(engineController1, engineController2), matchResult)
                .printReport(System.out);

         */
    }

    private static Stream<FEN> getFenList() {
        //List<String> fenList = List.of(FENDecoder.INITIAL_FEN);
        //List<String> fenList =  List.of("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList =  List.of(FENDecoder.INITIAL_FEN, "1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top25.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        Stream<PGN> pgnStream = new PGNStringDecoder().decodePGNs(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_v500.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_v2724.pgn"));
        return pgnStream.map(PGN::toGame).map(Game::getCurrentFEN);
    }

    private final Supplier<EngineController> mainEngineSupplier;
    private final Supplier<EngineController> oppponentEngineSupplier;

    public MatchMain(Supplier<EngineController> mainEngineSupplier, Supplier<EngineController> oppponentEngineSupplier) {
        this.mainEngineSupplier = mainEngineSupplier;
        this.oppponentEngineSupplier = oppponentEngineSupplier;
    }

    private List<MatchResult> play() {

        try (ObjectPool<EngineController> mainPool = new GenericObjectPool<>(new EngineControllerPoolFactory(mainEngineSupplier));
             ObjectPool<EngineController> opponentPool = new GenericObjectPool<>(new EngineControllerPoolFactory(oppponentEngineSupplier))) {

            MatchMultiple match = new MatchMultiple(mainPool, opponentPool, MATCH_TYPE)
                    .setDebugEnabled(MATCH_DEBUG)
                    .setSwitchChairs(MATCH_SWITCH_CHAIRS)
                    .setMatchListener(new MatchBroadcaster()
                            .addListener(new MatchListenerToMBean())
                            .addListener(new SavePGNGame()));

            Instant start = Instant.now();

            List<MatchResult> matchResult = match.play(getFenList());

            logger.info("Time taken: {} ms", Duration.between(start, Instant.now()).toMillis());

            return matchResult;
        }
    }
}
