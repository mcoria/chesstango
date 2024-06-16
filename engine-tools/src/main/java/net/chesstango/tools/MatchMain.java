package net.chesstango.tools;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterialAndPST;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp02;
import net.chesstango.tools.search.reports.arena.SummaryReport;
import net.chesstango.uci.arena.MatchMultiple;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.matchtypes.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchMain {
    private static final Logger logger = LoggerFactory.getLogger(MatchMain.class);

    private static final MatchType MATCH_TYPE = new MatchByDepth(4);
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
        EngineControllerPoolFactory tangoControllerFactory = new EngineControllerPoolFactory(() ->
                EngineControllerFactory
                        .createTangoControllerWithDefaultSearch(EvaluatorSEandImp02::new)
                        /*
                        .createTangoControllerWithDefaultEvaluator(AlphaBetaBuilder.class,
                        builder -> builder
                                .withGameEvaluatorCache()
                                .withQuiescence()
                                .withTranspositionTable()
                                .withTranspositionMoveSorter()
                                .withAspirationWindows()
                                .withIterativeDeepening()
                                .withStopProcessingCatch()
                                .withStatistics()
                        );*/
        );

        /*
        EngineControllerPoolFactory opponentControllerFactory = new EngineControllerPoolFactory(() ->
                EngineControllerFactory
                        .createProxyController("Spike", null));
         */

        EngineControllerPoolFactory opponentControllerFactory = new EngineControllerPoolFactory(() ->
                EngineControllerFactory
                        .createTangoControllerWithDefaultSearch(EvaluatorByMaterialAndPST::new)
                        /*
                        .createTangoControllerWithDefaultEvaluator(AlphaBetaBuilder.class,
                        builder -> builder
                                .withGameEvaluatorCache()
                                .withQuiescence()
                                .withTranspositionTable()
                                .withTranspositionMoveSorter()
                                .withAspirationWindows()
                                .withIterativeDeepening()
                                .withStopProcessingCatch()
                                .withStatistics()
                        );*/
        );


        List<MatchResult> matchResult = new MatchMain(tangoControllerFactory, opponentControllerFactory)
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

    private static List<String> getFenList() {
        //List<String> fenList = List.of(FENDecoder.INITIAL_FEN);
        //List<String> fenList =  List.of("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList =  List.of(FENDecoder.INITIAL_FEN, "1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top25.pgn"));
        List<String> fenList =  new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        //List<String> fenList =  new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_v500.pgn"));
        return fenList;
    }

    private final EngineControllerPoolFactory controllerFactory1;
    private final EngineControllerPoolFactory controllerFactory2;

    public MatchMain(EngineControllerPoolFactory controllerFactory1, EngineControllerPoolFactory controllerFactory2) {
        this.controllerFactory1 = controllerFactory1;
        this.controllerFactory2 = controllerFactory2;
    }

    private List<MatchResult> play() {
        MatchMultiple match = new MatchMultiple(controllerFactory1, controllerFactory2, MATCH_TYPE)
                .setDebugEnabled(MATCH_DEBUG)
                .setSwitchChairs(MATCH_SWITCH_CHAIRS)
                .setMatchListener(new MatchBroadcaster()
                        .addListener(new MatchListenerToMBean())
                        .addListener(new SavePGNGame()));

        Instant start = Instant.now();

        List<MatchResult> matchResult = match.play(getFenList());

        logger.info("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        return matchResult;
    }
}
