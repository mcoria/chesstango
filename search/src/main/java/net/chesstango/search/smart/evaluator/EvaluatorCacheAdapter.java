package net.chesstango.search.smart.evaluator;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

/**
 *
 * @author Mauricio Coria
 */
@Setter
public class EvaluatorCacheAdapter implements Evaluator, Acceptor {

    private Game game;

    @Getter
    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        this.evaluator.setGame(game);
    }

    @Override
    public int evaluate() {
        return evaluator.evaluate();
    }

    /*
    @Override
    public int evaluate() {
        long hash = game.getPosition().getZobristHash();

        int idx = (int) Math.abs(hash % ARRAY_SIZE);

        EvaluatorCacheArray.GameEvaluatorCacheEntry entry = cache[idx];

        if (entry.hash != hash || entry.age > currentAge || currentAge - entry.age >= STALE_AGE) {
            entry.hash = hash;
            entry.evaluation = evaluator.evaluate();
            entry.age = currentAge;
        } else {
            evaluationsCacheHitsCounter++;
        }

        return entry.evaluation;
    }
     */

}
