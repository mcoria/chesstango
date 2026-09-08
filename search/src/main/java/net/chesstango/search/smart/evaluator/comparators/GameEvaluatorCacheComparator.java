package net.chesstango.search.smart.evaluator.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.smart.evaluator.EvaluatorCacheEntry;
import net.chesstango.search.sorters.MoveComparator;
import net.chesstango.search.sorters.SortListener;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorCacheComparator implements MoveComparator, Acceptor, SortListener {

    @Getter
    @Setter
    private MoveComparator next;

    @Getter
    @Setter
    private EvaluatorCache evaluatorCache;

    @Setter
    private Game game;

    @Setter
    private MoveToHashMap moveToZobrist;

    private Color currentTurn;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(final int currentPly) {
        this.currentTurn = game.getPosition().getCurrentTurn();
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        final EvaluatorCacheEntry moveEvaluation1 = evaluatorCache.read(getZobristHashMove(o1));
        final EvaluatorCacheEntry moveEvaluation2 = evaluatorCache.read(getZobristHashMove(o2));

        if (moveEvaluation1 != null && moveEvaluation2 != null) {
            int evaluation1 = moveEvaluation1.getEvaluation();
            int evaluation2 = moveEvaluation2.getEvaluation();
            result = Color.WHITE.equals(currentTurn) ? Integer.compare(evaluation1, evaluation2) : Integer.compare(evaluation2, evaluation1);
        } else if (moveEvaluation1 != null) {
            return 1;
        } else if (moveEvaluation2 != null) {
            return -1;
        }

        return result == 0 ? next.compare(o1, o2) : result;
    }

    private long getZobristHashMove(Move move) {
        long hash = moveToZobrist.read(move);
        if (hash == 0) {
            hash = move.getZobristHash();
            moveToZobrist.write(move, hash);
        }
        return hash;
    }

}
