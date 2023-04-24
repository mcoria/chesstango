package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.util.List;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class SearchMoveResult {
    private final int depth;
    private final int evaluation;
    private final Move bestMove;
    private final Move ponderMove;
    private int evaluationCollisions;
    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] evaluatedNodes;
    private Set<Move>[] distinctMovesPerLevel;

    private List<Move> bestMoveOptions;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.evaluation = evaluation;
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
    }

    public int getDepth() {
        return depth;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public int getEvaluationCollisions() {
        return evaluationCollisions;
    }

    public SearchMoveResult setEvaluationCollisions(int evaluationCollisions) {
        this.evaluationCollisions = evaluationCollisions;
        return this;
    }


    public Move getBestMove() {
        return bestMove;
    }

    public Move getPonderMove() {
        return ponderMove;
    }

    public int[] getVisitedNodesCounters() {
        return visitedNodesCounters;
    }

    public SearchMoveResult setVisitedNodesCounters(int[] visitedNodesCounters) {
        this.visitedNodesCounters = visitedNodesCounters;
        return this;
    }

    public Set<Move>[] getDistinctMovesPerLevel() {
        return distinctMovesPerLevel;
    }

    public SearchMoveResult setDistinctMovesPerLevel(Set<Move>[] distinctMovesPerLevel) {
        this.distinctMovesPerLevel = distinctMovesPerLevel;
        return this;
    }

    public int[] getExpectedNodesCounters() {
        return expectedNodesCounters;
    }

    public SearchMoveResult setExpectedNodesCounters(int[] expectedNodesCounters) {
        this.expectedNodesCounters = expectedNodesCounters;
        return this;
    }

    public List<Move> getBestMoveOptions() {
        return bestMoveOptions;
    }

    public SearchMoveResult setBestMoveOptions(List<Move> bestMoveOptions) {
        this.bestMoveOptions = bestMoveOptions;
        return this;
    }

    public int[] getEvaluatedNodes() {
        return evaluatedNodes;
    }

    public SearchMoveResult setEvaluatedNodes(int[] evaluatedNodes) {
        this.evaluatedNodes = evaluatedNodes;
        return this;
    }
}
