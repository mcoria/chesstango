package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.alphabeta.filters.GameEvaluatorCounter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */

@Accessors(chain = true)
@Getter
@Setter
public class SearchMoveResult {
    private final int depth;

    private final int evaluation;

    private final Move bestMove;

    private final Move ponderMove;

    private int evaluationCollisions;

    private int[] visitedNodesCounters;

    private int[] visitedNodesQuiescenceCounter;

    private int[] expectedNodesCounters;

    private int[] evaluatedNodes;

    private Set<Move>[] distinctMovesPerLevel;

    private List<Move> bestMoveOptions;

    private long evaluatedGamesCounter;

    private List<Move> principalVariation;

    private Collection<MoveEvaluation> moveEvaluations;

    private Set<GameEvaluatorCounter.EvaluationEntry> evaluations;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.evaluation = evaluation;
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
    }

    public static class MoveEvaluation implements Comparable<MoveEvaluation> {
        public Move move;
        public int evaluation;

        @Override
        public int compareTo(MoveEvaluation other) {
            return Integer.compare(evaluation, other.evaluation);
        }
    }

}
