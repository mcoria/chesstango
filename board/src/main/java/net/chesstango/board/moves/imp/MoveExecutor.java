package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
interface MoveExecutor<T> {
    void apply(PiecePositioned from, PiecePositioned to, T layer);
}
