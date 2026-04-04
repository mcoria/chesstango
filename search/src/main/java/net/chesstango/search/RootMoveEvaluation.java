package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record RootMoveEvaluation(Move move,
                                 int evaluation,
                                 Bound bound) implements Serializable {
}
