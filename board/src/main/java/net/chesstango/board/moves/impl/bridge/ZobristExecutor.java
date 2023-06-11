package net.chesstango.board.moves.impl.bridge;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
interface ZobristExecutor {
    void apply(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, PositionStateReader oldPositionState, PositionStateReader newPositionState);
}
