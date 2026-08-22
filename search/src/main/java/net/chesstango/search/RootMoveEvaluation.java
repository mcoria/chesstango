package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record RootMoveEvaluation(Move move,
                                 int evaluation,
                                 Bound bound,
                                 PrincipalVariation pv) implements Serializable {

    @Override
    public String toString() {
        return String.format("RootMoveEvaluation[move=%s, evaluation=%d, bound=%s]", move, evaluation, bound);
    }

}
