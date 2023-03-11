package net.chesstango.search;

import net.chesstango.board.moves.Move;

public class SearchMoveResult {
    private final int depth;
    private final int evaluation;
    private final Move bestMove;
    private final Move ponderMove;
    private final int evaluationCollisions;

    private int[] visitedNodesCounter;

    private int[] evaluatedNodes;

    public SearchMoveResult(int depth, int evaluation, int evaluationCollisions, Move bestMove, Move ponderMove) {
        this.depth = depth;
        this.evaluation = evaluation;
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
        this.evaluationCollisions = evaluationCollisions;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public int getEvaluationCollisions() {
        return evaluationCollisions;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public Move getPonderMove() {
        return ponderMove;
    }
    public int[] getVisitedNodesCounter() {
        return visitedNodesCounter;
    }

    public void setVisitedNodesCounter(int[] visitedNodesCounter) {
        this.visitedNodesCounter = visitedNodesCounter;
    }

    public int[] getEvaluatedNodes() {
        return evaluatedNodes;
    }

    public void setEvaluatedNodes(int[] evaluatedNodes) {
        this.evaluatedNodes = evaluatedNodes;
    }
}
