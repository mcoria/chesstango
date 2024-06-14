package net.chesstango.tools;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp03;
import net.chesstango.tools.search.reports.arena.SummaryReport;
import net.chesstango.uci.arena.Match;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
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

    private static final MatchType MATCH_TYPE = new MatchByDepth(1);

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
        EngineController engineController1 = EngineControllerFactory
                .createTangoControllerWithDefaultSearch(() -> new EvaluatorSEandImp03(307, 177, 6, 510) );
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


        EngineController engineController2 = EngineControllerFactory
                .createProxyController("Spike", null);


        List<MatchResult> matchResult = new MatchMain(engineController1, engineController2).play();


        // Solo para ordenar la tabla de salida se especifican los engines en la lista

        new SummaryReport()
                .withSingleEngineInstance(List.of(engineController1, engineController2), matchResult)
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
        List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top25.pgn"));
        //List<String> fenList =  new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        //List<String> fenList = List.of("6k1/8/5Q2/1K6/8/8/8/8 b - - 50 148");
        return fenList;
    }

    private final EngineController engineController1;
    private final EngineController engineController2;

    public MatchMain(EngineController engineController1, EngineController engineController2) {
        this.engineController1 = engineController1;
        this.engineController2 = engineController2;
    }

    private List<MatchResult> play() {
        Match match = new Match(engineController1, engineController2, MATCH_TYPE)
                .setDebugEnabled(MATCH_DEBUG)
                .setSwitchChairs(MATCH_SWITCH_CHAIRS)
                .setMatchListener(new MatchBroadcaster()
                        .addListener(new MatchListenerToMBean())
                        .addListener(new SavePGNGame()));

        startEngines();

        Instant start = Instant.now();

        List<MatchResult> matchResult = match.play(getFenList());

        logger.info("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        quitEngines();

        return matchResult;
    }

    private void startEngines() {
        engineController1.startEngine();
        engineController2.startEngine();
    }

    private void quitEngines() {
        engineController1.send_CmdQuit();
        engineController2.send_CmdQuit();
    }
}
