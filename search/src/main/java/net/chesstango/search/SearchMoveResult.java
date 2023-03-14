package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.util.List;
import java.util.Set;

public class SearchMoveResult {
    private final int depth;
    private final int evaluation;
    private final Move bestMove;
    private final Move ponderMove;
    private int evaluationCollisions;
    private int[] visitedNodesCounters;
    private int[] evaluatedNodes;
    private List<Set<Move>> distinctMoves;

    public SearchMoveResult(int depth, int evaluation, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.evaluation = evaluation;
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
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

    public int[] getEvaluatedNodes() {
        return evaluatedNodes;
    }

    public SearchMoveResult setEvaluatedNodes(int[] evaluatedNodes) {
        this.evaluatedNodes = evaluatedNodes;
        return this;
    }

    public List<Set<Move>> getDistinctMoves() {
        return distinctMoves;
    }

    public SearchMoveResult setDistinctMoves(List<Set<Move>> distinctMoves) {
        this.distinctMoves = distinctMoves;
        return this;
    }
}
