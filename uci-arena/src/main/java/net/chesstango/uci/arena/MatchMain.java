package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.mbeans.Arena;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.reports.CutoffReports;
import net.chesstango.uci.arena.reports.GameReports;
import net.chesstango.uci.arena.reports.SessionReports;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.protocol.requests.CmdGo;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchMain implements MatchListener {
    private static final int DEPTH = 3;
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
                                            .createTangoController(GameEvaluatorSEandImp02.class);

        EngineController engineController2 = EngineControllerFactory
                                            .createProxyController("Spike", engineProxy -> engineProxy.setLogging(false))
                                            .overrideCmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));


        List<GameResult> matchResult = new MatchMain(engineController1, engineController2).play();

        // Solo para ordenar la tabla de salida se especifican los engines en la lista

        new GameReports()
                .printEngineControllersReport(Arrays.asList(engineController1, engineController2), matchResult);



        new SessionReports()
                 //.withCollisionStatics()
                 .withNodesVisitedStatics()
                 .withMovesPerLevelStatics()
                 .withCutoffStatics()
                 //.breakByColor()
                 .printTangoStatics(Arrays.asList(engineController1), matchResult);




        new CutoffReports()
                .printTangoStatics(Arrays.asList(engineController1), matchResult);


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
        //List<String> fenList =  Arrays.asList(FENDecoder.INITIAL_FEN);
        //List<String> fenList =  Arrays.asList("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        List<String> fenList =  Arrays.asList(FENDecoder.INITIAL_FEN, "1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        //List<String> fenList =  new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        //List<String> fenList = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        return fenList;
    }

    private List<GameResult> play() {
        MatchBroadcaster matchBroadcaster = new MatchBroadcaster();
        matchBroadcaster.addListener(new MatchListenerToMBean(arenaMBean));
        matchBroadcaster.addListener(this);

        Match match = new Match(engineController1, engineController2, DEPTH)
                            .setDebugEnabled(MATCH_DEBUG)
                            //.switchChairs(false)
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
