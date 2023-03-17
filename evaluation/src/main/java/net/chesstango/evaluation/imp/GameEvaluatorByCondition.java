package net.chesstango.evaluation.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GameEvaluatorByCondition implements GameEvaluator {
    private int defaultValue;

    private List<Function<Game, Integer>> evaluationConditions = new ArrayList<>();

    @Override
    public int evaluate(final Game game) {
        return game.getStatus().isFinalStatus() ? evaluateFinalStatus(game) : evaluateNonFinalStatus(game);
    }

    protected int evaluateNonFinalStatus(final Game game) {
        Integer evaluationResult = null;
        for (Function<Game, Integer> evaluationCondition: evaluationConditions) {
            evaluationResult =  evaluationCondition.apply(game);
            if(evaluationResult != null ) break;
        }

        return evaluationResult == null ? defaultValue : evaluationResult.intValue();
    }

    protected int evaluateFinalStatus(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case CHECK:
            case NO_CHECK:
                throw new RuntimeException("Game is still in progress");
        }
        return evaluation;
    }

    public GameEvaluatorByCondition setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public GameEvaluatorByCondition addCondition(Function<Game, Integer> evaluationCondition) {
        evaluationConditions.add(evaluationCondition);
        return this;
    }

}
