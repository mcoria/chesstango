package net.chesstango.search;

import net.chesstango.board.moves.Move;

public class SearchMoveResult {
    private final int evaluation;
    private final Move bestMove;
    private final Move ponderMove;
    private final int evaluationCollisions;

    public SearchMoveResult(int evaluation, int evaluationCollisions, Move bestMove, Move ponderMove) {
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
}
