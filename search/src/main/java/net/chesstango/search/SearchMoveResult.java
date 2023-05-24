package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.util.Collection;
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
    private int[] visitedNodesQuiescenceCounter;
    private int[] expectedNodesCounters;
    private int[] evaluatedNodes;
    private Set<Move>[] distinctMovesPerLevel;
    private List<Move> bestMoveOptions;
    private long evaluatedGamesCounter;
    private List<Move> principalVariation;
    private Collection<MoveEvaluation> moveEvaluations;


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

    public int[] getVisitedNodesQuiescenceCounter() {
        return visitedNodesQuiescenceCounter;
    }

    public SearchMoveResult setVisitedNodesQuiescenceCounter(int[] visitedNodesQuiescenceCounter) {
        this.visitedNodesQuiescenceCounter = visitedNodesQuiescenceCounter;
        return this;
    }

    public long getEvaluatedGamesCounter() {
        return evaluatedGamesCounter;
    }

    public SearchMoveResult setEvaluatedGamesCounter(long evaluatedGamesCounter) {
        this.evaluatedGamesCounter = evaluatedGamesCounter;
        return this;
    }

    public List<Move> getPrincipalVariation() {
        return principalVariation;
    }

    public Collection<MoveEvaluation> getMoveEvaluations() {
        return moveEvaluations;
    }

    public SearchMoveResult setPrincipalVariation(List<Move> principalVariation) {
        this.principalVariation = principalVariation;
        return this;
    }

    public SearchMoveResult setMoveEvaluations(Collection<MoveEvaluation> moveEvaluations) {
        this.moveEvaluations = moveEvaluations;
        return this;
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
