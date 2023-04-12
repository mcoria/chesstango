package net.chesstango.uci.arena;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.Transcoding;
import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.mbeans.Arena;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.arena.mbeans.MatchListenerImp;
import net.chesstango.uci.arena.reports.GameReports;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.proxy.ProxyConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

    public static void main(String[] args) {
        EngineController engineController1 = createTangoController(GameEvaluatorSEandImp02.class);

        //EngineController engineController2 = new EngineControllerImp(new EngineProxy(ProxyConfig.loadEngineConfig("GameEvaluatorImp02")).setLogging(false)).overrideEngineName("GameEvaluatorImp02");
        //EngineController engineController2 = createTangoController(GameEvaluatorImp02.class);
        EngineController engineController2 = new EngineControllerImp(new EngineProxy(ProxyConfig.loadEngineConfig("Spike"))
                .setLogging(false))
                .overrideCmdGo(new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));

        List<GameResult> matchResult = new MatchMain(engineController1, engineController2).play();

        // Solo para ordenar la tabla de salida se especifican los engines en la lista
        new GameReports()
                .printEngineControllersReport(Arrays.asList(engineController1, engineController2), matchResult);

        /*
        new SessionReports()
                 //.withCollisionStatics()
                 .withNodesVisitedStatics()
                 .withMovesPerLevelStatics()
                 .withPrintCutoffStatics()
                 .breakByColor()
                 .printTangoStatics(Arrays.asList(engineController1, engineController2), matchResult);


        new CutoffReports()
                .printTangoStatics(Arrays.asList(engineController1), matchResult);

         */
    }

    private final Arena arenaMBean;
    private final EngineController engineController1;
    private final EngineController engineController2;

    public MatchMain(EngineController engineController1, EngineController engineController2) {
        this.arenaMBean = new Arena();
        this.engineController1 = engineController1;
        this.engineController2 = engineController2;
    }

    private static List<String> getFenList() {
        //return Arrays.asList(FENDecoder.INITIAL_FEN);
        //return Arrays.asList("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");
        return new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        //return new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
    }

    private List<GameResult> play() {
        arenaMBean.registerMBean();

        Match match = new Match(engineController1, engineController2, DEPTH)
                .setDebugEnabled(MATCH_DEBUG)
                .switchChairs(true)
                .setMatchListener(new MatchBroadcaster()
                        .addListener(new MatchListenerImp(arenaMBean))
                        .addListener(this)
                );

        startEngines(engineController1, engineController2);

        Instant start = Instant.now();
        List<GameResult> matchResult = match.play(getFenList());
        System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        quitEngines(engineController1, engineController2);

        return matchResult;
    }

    public void startEngines(EngineController engine1, EngineController engine2) {
        engine1.startEngine();
        engine2.startEngine();
    }

    public void quitEngines(EngineController engine1, EngineController engine2) {
        engine1.send_CmdQuit();
        engine2.send_CmdQuit();
    }

    private static void save(GameResult gameResult) {
        PGNEncoder encoder = new PGNEncoder();
        String encodedGame = encoder.encode(gameResult.getPgnGame());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./matches.pgn", true));
            writer.append(encodedGame);
            writer.append("\n\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static EngineController createTangoController(Class<? extends GameEvaluator> gameEvaluatorClass) {
        EngineTango tango = createEngineTango(gameEvaluatorClass);
        EngineControllerImp controller = new EngineControllerImp(tango);
        controller.overrideEngineName(gameEvaluatorClass.getSimpleName());
        return controller;
    }

    private static EngineTango createEngineTango(Class<? extends GameEvaluator> gameEvaluatorClass) {
        DefaultSearchMove search = new DefaultSearchMove();
        try {
            search.setGameEvaluator(gameEvaluatorClass.getDeclaredConstructor().newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return new EngineTango(search);
    }

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
    }

    @Override
    public void notifyMove(Game game, Move move) {
    }

    @Override
    public void notifyEndGame(GameResult gameResult) {
        save(gameResult);
    }
}
