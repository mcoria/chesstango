package net.chesstango.board.internal.moves;

import net.chesstango.board.PiecePositioned;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface MoveExecutorLayer<T> {
    void apply(PiecePositioned from, PiecePositioned to, T layer);
}
