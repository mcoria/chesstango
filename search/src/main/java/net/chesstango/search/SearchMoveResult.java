package net.chesstango.search;

import net.chesstango.board.moves.Move;

public class SearchMoveResult {
    private int evaluation;
    private Move bestMove;
    private Move ponderMove;

    public SearchMoveResult(int evaluation, Move bestMove, Move ponderMove) {
        this.evaluation = evaluation;
        this.bestMove = bestMove;
        this.ponderMove = ponderMove;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public Move getPonderMove() {
        return ponderMove;
    }
}
