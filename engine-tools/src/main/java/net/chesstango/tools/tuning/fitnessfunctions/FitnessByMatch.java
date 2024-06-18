package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.arena.MatchMultiple;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.matchtypes.MatchType;
import net.chesstango.uci.engine.UciTango;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByMatch implements FitnessFunction {
    private static final MatchType MATCH_TYPE = new MatchByDepth(2);

    private static final String ENGINE_NAME = "TANGO";

    private ObjectPool<EngineController> opponentPool;

    private List<String> fenList;


    @Override
    public void start() {
        Supplier<EngineController> opponentSupplier = () -> EngineControllerFactory.createProxyController("Spike", null);

        //this.fenList = new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top10.pgn"));
        this.fenList = new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top25.pgn"));
        //this.fenList = new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
        //this.fenList = new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_v500.pgn"));
        this.opponentPool = new GenericObjectPool<>(new EngineControllerPoolFactory(opponentSupplier));
    }

    @Override
    public void stop() {
        opponentPool.close();
    }

    @Override
    public long fitness(Supplier<Evaluator> tangoEvaluatorSupplier) {
        Supplier<EngineController> tangoEngineSupplier = () ->
                new EngineControllerImp(new UciTango(new Tango(new DefaultSearchMove(tangoEvaluatorSupplier.get()))))
                        .overrideEngineName(ENGINE_NAME);

        List<MatchResult> matchResult = fitnessEval(tangoEngineSupplier);

        return calculatePoints(matchResult);
    }


    private List<MatchResult> fitnessEval(Supplier<EngineController> tangoEngineSupplier) {
        try (ObjectPool<EngineController> tangoPool = new GenericObjectPool<>(new EngineControllerPoolFactory(tangoEngineSupplier))) {
            return new MatchMultiple(tangoPool, opponentPool, MATCH_TYPE)
                    .setSwitchChairs(true)
                    .setMatchListener(new MatchBroadcaster()
                            .addListener(new SavePGNGame()))
                    .play(fenList);
        }
    }


    protected long calculatePoints(List<MatchResult> matchResult) {
        long pointsWhiteWin = matchResult.stream()
                .filter(result -> ENGINE_NAME.equals(result.getEngineWhite().getEngineName()) && result.getEngineWhite() == result.getWinner())
                .count();

        long pointsWhiteLost = matchResult.stream()
                .filter(result -> ENGINE_NAME.equals(result.getEngineWhite().getEngineName()) && result.getEngineBlack() == result.getWinner())
                .count();

        long pointsBlackWin = matchResult.stream()
                .filter(result -> ENGINE_NAME.equals(result.getEngineBlack().getEngineName()) && result.getEngineBlack() == result.getWinner())
                .count();

        long pointsBlackLost = matchResult.stream()
                .filter(result -> ENGINE_NAME.equals(result.getEngineBlack().getEngineName()) && result.getEngineWhite() == result.getWinner())
                .count();

        return pointsWhiteWin - pointsWhiteLost + pointsBlackWin - pointsBlackLost;
    }

}
