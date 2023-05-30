package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.Transcoding;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.evaluation.imp.GameEvaluatorSimplifiedEvaluator;
import net.chesstango.mbeans.Arena;
import net.chesstango.search.builders.MinMaxBuilder;
import net.chesstango.search.builders.MinMaxPruningBuilder;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.reports.SearchesReport;
import net.chesstango.uci.arena.reports.SessionReport;
import net.chesstango.uci.arena.reports.SummaryReport;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerFactory;
import net.chesstango.uci.protocol.requests.CmdGo;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchMain implements MatchListener {

    private static final CmdGo CMD_GO = new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(4);
    //private static final CmdGo CMD_GO = new CmdGo().setGoType(CmdGo.GoType.MOVE_TIME).setTimeOut(500);

    private static final boolean MATCH_DEBUG = false;

    /**
     * -Dcom.sun.management.jmxremote
     * -Dcom.sun.management.jmxremote.port=19999
     * -Dcom.sun.management.jmxremote.local.only=false
     * -Dcom.sun.management.jmxremote.authenticate=false
     * -Dcom.sun.management.jmxremote.ssl=false
     *
     */
    public static void main(String[] args) {
        EngineController engineController1 = EngineControllerFactory
                .createTangoControllerWithDefaultSearch(GameEvaluatorSEandImp02.class);
                //.createTangoControllerWithDefaultEvaluator(MinMaxPruningBuilder.class, minMaxPruningBuilder -> minMaxPruningBuilder.withStatics() )
                //.overrideEngineName("MinMaxPruning");



        /*
        EngineController engineController2 = EngineControllerFactory
                .createTangoControllerWithDefaultSearch(GameEvaluatorSimplifiedEvaluator.class);
               // .createTangoControllerWithDefaultEvaluator(MinMaxBuilder.class, null )
                //.overrideEngineName("MinMax");


*/
        EngineController engineController2 = EngineControllerFactory
                                            .createProxyController("Spike", engineProxy -> engineProxy.setLogging(false));
                                            //.overrideCmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));



        List<GameResult> matchResult = new MatchMain(engineController1, engineController2).play();

        // Solo para ordenar la tabla de salida se especifican los engines en la lista


        new SummaryReport()
                .printReportSingleEngineInstance(Arrays.asList(engineController1, engineController2), matchResult);




        new SessionReport()
                 //.withCollisionStatics()
                 .withNodesVisitedStatics()
                 .withMovesPerLevelStatics()
                 .withCutoffStatics()
                 //.breakByColor()
                 .printTangoStatics(Arrays.asList(engineController1, engineController2), matchResult);


        new SearchesReport()
                .withCutoffStatics()
                .withNodesVisitedStatics()
                .withPrincipalVariation()
                .printTangoStatics(Arrays.asList(engineController1, engineController2), matchResult);

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
        List<String> fenList =  Arrays.asList(FENDecoder.INITIAL_FEN);
        //List<String> fenList =  Arrays.asList("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList =  Arrays.asList(FENDecoder.INITIAL_FEN, "1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        //List<String> fenList =  new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        return fenList;
    }

    private List<GameResult> play() {
        MatchBroadcaster matchBroadcaster = new MatchBroadcaster();
        matchBroadcaster.addListener(new MatchListenerToMBean(arenaMBean));
        matchBroadcaster.addListener(this);

        Match match = new Match(engineController1, engineController2, CMD_GO)
                            .setDebugEnabled(MATCH_DEBUG)
                            .switchChairs(false)
                            .setMatchListener(matchBroadcaster);

        startEngines();

        Instant start = Instant.now();
        List<GameResult> matchResult = match.play(getFenList());
        System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms");

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
    public void notifyEndGame(Game game, GameResult gameResult) {
        gameResult.save();
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
