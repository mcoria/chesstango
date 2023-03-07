package net.chesstango.uci.arena;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.evaluation.imp.*;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.search.SearchMove;
import net.chesstango.uci.arena.reports.Reports;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.gui.EngineControllerImp;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.proxy.ProxyConfig;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchMain {

    public static final List<String> GAMES_BALSA_TOP10 = Arrays.asList(
            "r2qkbnr/pp1n1ppp/2p1p3/3pPb2/3P4/2P2N2/PP2BPPP/RNBQK2R b KQkq - 3 6",
            "r1bqkb1r/1p3ppp/p1nppn2/6B1/3NP3/2N5/PPPQ1PPP/2KR1B1R b kq - 1 8",
            "rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8",
            "rn1qkb1r/1p3ppp/p2pbn2/4p3/4P3/1NN1BP2/PPP3PP/R2QKB1R b KQkq - 0 8",
            "r1bqkb1r/pp1n1ppp/2n1p3/2ppP3/3P1P2/2N1BN2/PPP3PP/R2QKB1R b KQkq - 3 7",
            "r1bq1rk1/ppp2ppp/2np1n2/2b1p3/2B1P3/2PP1N2/PP3PPP/RNBQ1RK1 w - - 2 7",
            "r1bq1rk1/2ppbppp/p1n2n2/1p2p3/4P3/1B3N2/PPPP1PPP/RNBQR1K1 w - - 2 8",
            "r1b1kb1r/pp1n1pp1/2p1pq1p/3p4/2PP4/2N1PN2/PP3PPP/R2QKB1R w KQkq - 1 8",
            "r1bqk2r/pp1n1ppp/2pbpn2/3p4/2PP4/2N1PN2/PPQ2PPP/R1B1KB1R w KQkq - 4 7",
            "rn1qk2r/p1pp1ppp/bp2pn2/8/1bPP4/1P3NP1/P2BPP1P/RN1QKB1R b KQkq - 2 6");

    public static void main(String[] args) {
        //EngineController controllerTango = new EngineControllerImp(new EngineTango(new Dummy()).enableAsync());
        SearchMove search = new DefaultSearchMove();
        search.setGameEvaluator(new GameEvaluatorImp03());
        EngineController controllerTango = new EngineControllerImp(new EngineTango(search));
        EngineController controllerOponente = new EngineControllerImp(new EngineProxy(ProxyConfig.loadEngineConfig("Spike")).setLogging(false));


        Match match = new Match(controllerTango, controllerOponente, 1);
        //match.setDebugEnabled(true);

        List<String> fenPositions = new Transcoding().pgnFileToFenPositions(MatchMain.class.getClassLoader().getResourceAsStream("Balsa_v2724.pgn"));
        //List<String> fenPositions = Arrays.asList("r1bqkb1r/pp1n1ppp/3p1n2/1Bp1p3/P3P3/2N2N2/1PPP1PPP/R1BQ1RK1 b kq - 1 6");
        //List<String> fenPositions = Arrays.asList("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21");


        startEngines(controllerTango, controllerOponente);

        Instant start = Instant.now();
        List<GameResult> matchResult = match.play(fenPositions);
        System.out.println("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms");

        quitEngines(controllerTango, controllerOponente);

        new Reports().printEngineControllersReport(Arrays.asList(controllerTango, controllerOponente), matchResult);
    }

    public static void startEngines(EngineController engine1, EngineController engine2) {
        engine1.startEngine();
        engine2.startEngine();
    }

    public static void quitEngines(EngineController engine1, EngineController engine2) {
        engine1.send_CmdQuit();
        engine2.send_CmdQuit();
    }
}
