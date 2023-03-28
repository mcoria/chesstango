package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
interface ZobritExecutor {
    void apply(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);
}
