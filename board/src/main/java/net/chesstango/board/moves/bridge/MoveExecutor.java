package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;

@FunctionalInterface
public interface MoveExecutor<T> {
    void apply(PiecePositioned from, PiecePositioned to, T layer);
}
