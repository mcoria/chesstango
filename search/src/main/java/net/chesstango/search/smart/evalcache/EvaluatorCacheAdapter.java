package net.chesstango.search.smart.evalcache;

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

    @Getter
    private EvaluatorCache evaluatorCache;

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
        long hash = game.getPosition().getZobristHash();

        EvaluatorCacheEntry evaluatorCacheEntry = evaluatorCache.read(hash);

        if (evaluatorCacheEntry == null) {
            evaluatorCacheEntry = evaluatorCache.write(hash, evaluator.evaluate());
        }

        return evaluatorCacheEntry.evaluation;
    }
}
