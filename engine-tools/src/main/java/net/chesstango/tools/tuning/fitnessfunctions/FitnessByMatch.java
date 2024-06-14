package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.arena.Match;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.gui.ProxyConfigLoader;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.matchtypes.MatchType;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.proxy.UciProxy;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByMatch implements FitnessFunction {
    private static final MatchType matchType = new MatchByDepth(1);

    private final List<String> fenList;

    private ObjectPool<EngineController> pool;

    public FitnessByMatch() {
        this.fenList = getFenList();
    }

    @Override
    public void start() {
        pool = new GenericObjectPool<>(new EngineControllerPoolFactory(() -> new EngineControllerImp(new UciProxy(ProxyConfigLoader.loadEngineConfig("Spike")))));
    }

    @Override
    public void stop() {
        pool.close();
    }

    @Override
    public long fitness(Supplier<GameEvaluator> gameEvaluatorSupplier) {
        EngineController engineTango = createTango(gameEvaluatorSupplier.get());

        engineTango.startEngine();

        List<MatchResult> matchResult = fitnessEval(engineTango);

        engineTango.send_CmdQuit();

        return calculatePoints(engineTango, matchResult);
    }

    protected EngineController createTango(GameEvaluator gameEvaluator) {
        DefaultSearchMove search = new DefaultSearchMove(gameEvaluator);

        return new EngineControllerImp(new UciTango(new Tango(search)));
    }


    private List<MatchResult> fitnessEval(EngineController engineTango) {
        List<MatchResult> matchResult = null;
        try {

            EngineController engineProxy = pool.borrowObject();

            Match match = new Match(engineProxy, engineTango, matchType);
            match.setMatchListener(new MatchBroadcaster()
                    .addListener(new SavePGNGame()));

            matchResult = match.play(fenList);

            pool.returnObject(engineProxy);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
        return matchResult;
    }


    private static List<String> getFenList() {
        return new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        //return new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top25.pgn"));
        //return new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
    }


    protected long calculatePoints(EngineController engineTango, List<MatchResult> matchResult) {
        long pointsWhiteWin = matchResult.stream()
                .filter(result -> result.getEngineWhite() == engineTango && result.getEngineWhite() == result.getWinner())
                .count();

        long pointsWhiteLost = matchResult.stream()
                .filter(result -> result.getEngineWhite() == engineTango && result.getEngineBlack() == result.getWinner())
                .count();

        long pointsBlackWin = matchResult.stream()
                .filter(result -> result.getEngineBlack() == engineTango && result.getEngineBlack() == result.getWinner())
                .count();

        long pointsBlackLost = matchResult.stream()
                .filter(result -> result.getEngineBlack() == engineTango && result.getEngineWhite() == result.getWinner())
                .count();

        return pointsWhiteWin - pointsWhiteLost + pointsBlackWin - pointsBlackLost;
    }

}
