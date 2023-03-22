package net.chesstango.board.moves.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface ZobritExecutor {
    void apply(PiecePositioned from, PiecePositioned to, ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);
}
