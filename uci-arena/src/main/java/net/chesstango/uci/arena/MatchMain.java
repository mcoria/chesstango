package net.chesstango.uci.arena;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.arena.reports.Reports;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.engine.EngineTango;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class MatchMain {

    private static final List<String> GAMES = Arrays.asList(FENDecoder.INITIAL_FEN,
            "4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20",
            "r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7",
            "rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5");

    public static void main(String[] args) {
        EngineTango engineTango = new EngineTango();
        EngineController controllerTango = new EngineControllerImp(engineTango);
        EngineController controllerOponente = new EngineControllerImp(new EngineProxy());
        //EngineControllerImp engineOponente = new EngineControllerImp(new EngineTango(new Dummy()));

        Instant start = Instant.now();

        Match match = new Match(controllerTango, controllerOponente, 1);

        startEngines(controllerTango, controllerOponente);

        List<GameResult> matchResult = match.play(GAMES);

        quitEngines(controllerTango, controllerOponente);

        Duration timeElapsed = Duration.between(start, Instant.now());
        System.out.println("Time taken: " + timeElapsed.toMillis() + " ms");

        new Reports().printByEngine(controllerTango, controllerOponente, matchResult);

        new Reports().printTangoStatics(engineTango.getSessions(), false);
    }

    public static void startEngines(EngineController engine1, EngineController engine2) {
        engine1.send_CmdUci();
        engine1.send_CmdIsReady();

        engine2.send_CmdUci();
        engine2.send_CmdIsReady();
    }

    public static void quitEngines(EngineController engine1, EngineController engine2) {
        engine1.send_CmdQuit();
        engine2.send_CmdQuit();
    }
}
