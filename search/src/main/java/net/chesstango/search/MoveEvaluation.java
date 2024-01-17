package net.chesstango.search;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public record MoveEvaluation(Move move,
                             int evaluation,
                             MoveEvaluationType moveEvaluationType)
        implements Comparable<MoveEvaluation> {

    @Override
    public int compareTo(MoveEvaluation other) {
        return Integer.compare(evaluation, other.evaluation);
    }
}
