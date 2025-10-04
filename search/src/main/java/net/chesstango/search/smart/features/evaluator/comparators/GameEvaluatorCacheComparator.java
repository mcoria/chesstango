package net.chesstango.search.smart.features.evaluator.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorCacheComparator implements MoveComparator, SearchByCycleListener {

    @Getter
    @Setter
    private MoveComparator next;

    @Getter
    @Setter
    private EvaluatorCacheRead evaluatorCacheRead;

    @Setter
    private Game game;

    private MoveToHashMap moveToZobrist;
    private Color currentTurn;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
    }

    @Override
    public void beforeSort(final int currentPly, MoveToHashMap moveToZobrist) {
        this.moveToZobrist = moveToZobrist;

        this.currentTurn = game.getPosition().getCurrentTurn();

        next.beforeSort(currentPly, moveToZobrist);
    }

    @Override
    public void afterSort(int currentPly, MoveToHashMap moveToZobrist) {
        next.afterSort(currentPly, moveToZobrist);
    }

    @Override
    public int compare(Move o1, Move o2) {
        int result = 0;

        final Integer moveEvaluation1 = evaluatorCacheRead.readFromCache(getZobristHashMove(o1));
        final Integer moveEvaluation2 = evaluatorCacheRead.readFromCache(getZobristHashMove(o2));

        if (moveEvaluation1 != null && moveEvaluation2 != null) {
            int evaluation1 = TranspositionEntry.decodeValue(moveEvaluation1);
            int evaluation2 = TranspositionEntry.decodeValue(moveEvaluation2);
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
