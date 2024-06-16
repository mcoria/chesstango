package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.representations.Transcoding;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.DefaultSearchMove;
import net.chesstango.uci.arena.MatchMultiple;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineControllerFactory;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchBroadcaster;
import net.chesstango.uci.arena.listeners.MatchListenerToMBean;
import net.chesstango.uci.arena.listeners.SavePGNGame;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.arena.matchtypes.MatchType;
import net.chesstango.uci.engine.UciTango;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByMatch implements FitnessFunction {
    private static final MatchType MATCH_TYPE = new MatchByDepth(2);

    private static final String ENGINE_NAME = "TANGO";

    private List<String> fenList;

    @Override
    public void start() {
        this.fenList = new Transcoding().pgnFileToFenPositions(FitnessByMatch.class.getClassLoader().getResourceAsStream("Balsa_Top50.pgn"));
    }

    @Override
    public void stop() {
    }

    @Override
    public long fitness(Supplier<Evaluator> gameEvaluatorSupplier) {
        EngineControllerPoolFactory engineControllerPoolFactory = new EngineControllerPoolFactory(() ->
                new EngineControllerImp(new UciTango(new Tango(new DefaultSearchMove(gameEvaluatorSupplier.get()))))
                        .overrideEngineName(ENGINE_NAME)
        );

        EngineControllerPoolFactory opponentControllerPoolFactory = new EngineControllerPoolFactory(() ->
                EngineControllerFactory.createProxyController("Spike", null)
        );

        List<MatchResult> matchResult = fitnessEval(engineControllerPoolFactory, opponentControllerPoolFactory);

        return calculatePoints(matchResult);
    }

    protected EngineControllerPoolFactory createTango(Supplier<Evaluator> gameEvaluatorSupplier) {
        return new EngineControllerPoolFactory(() -> new EngineControllerImp(new UciTango(new Tango(new DefaultSearchMove(gameEvaluatorSupplier.get())))));
    }


    private List<MatchResult> fitnessEval(EngineControllerPoolFactory engineControllerPoolFactory,
                                          EngineControllerPoolFactory opponentControllerPoolFactory) {
        try {
            return new MatchMultiple(engineControllerPoolFactory, opponentControllerPoolFactory, MATCH_TYPE)
                    .setSwitchChairs(true)
                    .setMatchListener(new MatchBroadcaster()
                            .addListener(new MatchListenerToMBean())
                            .addListener(new SavePGNGame()))
                    .play(fenList);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
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
