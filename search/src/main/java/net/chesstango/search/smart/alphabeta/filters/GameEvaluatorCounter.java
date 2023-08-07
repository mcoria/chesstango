package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.util.*;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorCounter implements GameEvaluator, SearchLifeCycle {
    private GameEvaluator imp;
    private long counter;
    private Set<EvaluationEntry> evaluations;


    public GameEvaluatorCounter(GameEvaluator instance) {
        this.imp = instance;
    }

    @Override
    public int evaluate(Game game) {
        counter++;
        int evaluation = imp.evaluate(game);
        evaluations.add(new EvaluationEntry(game.getChessPosition().getZobristHash(), evaluation));
        return evaluation;
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        counter = 0;
        evaluations = new LinkedHashSet<>();
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setEvaluatedGamesCounter(counter);
            result.setEvaluations(evaluations);
        }
    }

    @Override
    public void init(SearchContext context) {
    }

    @Override
    public void close(SearchMoveResult result) {
        if (result != null) {
            result.setEvaluatedGamesCounter(counter);
            result.setEvaluations(evaluations);
        }
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

    public static class EvaluationEntry {

        private final long key;
        private final int value;

        public EvaluationEntry(long key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EvaluationEntry that = (EvaluationEntry) o;
            return key == that.key && value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        public long getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

    }
}
