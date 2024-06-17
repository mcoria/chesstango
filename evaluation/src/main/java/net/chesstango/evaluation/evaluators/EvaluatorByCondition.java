package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByCondition implements Evaluator {
    private final List<Function<Game, Integer>> evaluationConditions = new ArrayList<>();

    private int defaultValue;

    private Game game;

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int evaluate() {
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
            case STALEMATE:
                evaluation = 0;
                break;
            case CHECK:
            case NO_CHECK:
                throw new RuntimeException("Game is still in progress");
        }
        return evaluation;
    }

    public EvaluatorByCondition setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public EvaluatorByCondition addCondition(Function<Game, Integer> evaluationCondition) {
        evaluationConditions.add(evaluationCondition);
        return this;
    }

}
