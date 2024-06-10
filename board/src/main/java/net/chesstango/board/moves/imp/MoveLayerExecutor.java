package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
interface MoveLayerExecutor<T> {
    void apply(PiecePositioned from, PiecePositioned to, T layer);
}
