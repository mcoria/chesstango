package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.mbeans.Arena;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.reports.SearchesReport;
import net.chesstango.uci.arena.reports.SessionReport;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.protocol.requests.CmdGo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchMain implements MatchListener {

    private static final Logger logger = LoggerFactory.getLogger(MatchMain.class);

    private static final CmdGo CMD_GO = new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(2);
    //private static final CmdGo CMD_GO = new CmdGo().setGoType(CmdGo.GoType.MOVE_TIME).setTimeOut(300);

    private static final boolean MATCH_DEBUG = false;

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
                //.createTangoControllerWithDefaultSearch(GameEvaluatorSEandImp02.class)
                .createTangoControllerWithDefaultEvaluator(AlphaBetaBuilder.class, minMaxPruningBuilder -> minMaxPruningBuilder
                        .withQuiescence()

                        .withTranspositionTable()
                        .withQTranspositionTable()
                        //.withTranspositionTableReuse()

                        .withTranspositionMoveSorter()
                        .withQTranspositionMoveSorter()

                        .withStopProcessingCatch()
                        .withIterativeDeepening()

                        .withStatistics()
                );
                //.overrideEngineName("AB Full");



        /*
        EngineController engineController2 = EngineControllerFactory
                //.createTangoControllerWithDefaultSearch(GameEvaluatorSimplifiedEvaluator.class);
                //.createTangoControllerWithDefaultEvaluator(MinMaxBuilder.class, null )
                .createTangoControllerWithDefaultEvaluator(AlphaBetaBuilder.class, minMaxPruningBuilder -> minMaxPruningBuilder
                        .withQuiescence()
                        //.withTranspositionTable()
                        //.withQTranspositionTable()
                        //.withTranspositionTableReuse()
                        //.withTranspositionMoveSorter()
                        //.withQTranspositionMoveSorter()
                        //.withStopProcessingCatch()
                        //.withIterativeDeepening()
                        .withStatics()
                ).overrideEngineName("AB without TT" );
        */


        EngineController engineController2 = EngineControllerFactory
                .createProxyController("Spike", null);
        //.overrideEngineName("tango-v0.0.11");
        //.overrideCmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));


        List<MatchResult> matchResult = new MatchMain(engineController1, engineController2).play();

        // Solo para ordenar la tabla de salida se especifican los engines en la lista


        /*
        new SummaryReport()
                .withSingleEngineInstance(List.of(engineController1, engineController2), matchResult)
                //.withMatchResult(List.of(engineController1, engineController2), matchResult)
                .printReport(System.out);
        */


        new SessionReport()
                 //.withCollisionStatics()
                 .withNodesVisitedStatics()
                 //.withMovesPerLevelStatics()
                 //.withCutoffStatics()
                 //.breakByColor()
                 .withMathResults(List.of(engineController1, engineController2), matchResult)
                .printReport(System.out);



        new SearchesReport()
                //.withCutoffStatics()
                //.withNodesVisitedStatics()
                //.withPrincipalVariation()
                .withMathResults(List.of(engineController1, engineController2), matchResult)
                .printReport(System.out);


    }

    private final Arena arenaMBean;
    private final EngineController engineController1;
    private final EngineController engineController2;

    public MatchMain(EngineController engineController1, EngineController engineController2) {
        this.arenaMBean = Arena.createAndRegisterMBean();
        this.engineController1 = engineController1;
        this.engineController2 = engineController2;
    }

    private static List<String> getFenList() {
        List<String> fenList = Arrays.asList(FENDecoder.INITIAL_FEN);
        //List<String> fenList =  Arrays.asList("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList =  Arrays.asList(FENDecoder.INITIAL_FEN, "1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        //List<String> fenList =  new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        return fenList;
    }

    private List<MatchResult> play() {
        MatchBroadcaster matchBroadcaster = new MatchBroadcaster();
        matchBroadcaster.addListener(new MatchListenerToMBean(arenaMBean));
        matchBroadcaster.addListener(this);

        Match match = new Match(engineController1, engineController2, CMD_GO)
                .setDebugEnabled(MATCH_DEBUG)
                .switchChairs(true)
                .setMatchListener(matchBroadcaster);

        startEngines();

        Instant start = Instant.now();
        List<MatchResult> matchResult = match.play(getFenList());

        logger.info("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        quitEngines();

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
        matchResult.save();
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
