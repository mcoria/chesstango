package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.MinMaxPruningBuilder;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {
    private final SearchMove imp;

    public DefaultSearchMove() {
        this(new DefaultGameEvaluator());
    }

    public DefaultSearchMove(final GameEvaluator gameEvaluator) {
        this.imp = new MinMaxPruningBuilder()
                .withGameEvaluator(gameEvaluator)
                .withStatics()
                //.withDetectCycle()
                .withTranspositionTable()
                //.withQTranspositionTable()
                .withIterativeDeepening()
                //.withQuiescence()
                .build();
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return imp.searchBestMove(game);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        return imp.searchBestMove(game, depth);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }
}
