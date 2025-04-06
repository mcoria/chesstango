package net.chesstango.board.internal.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface MoveExecutorZobrist {
    void apply(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, PositionStateReader chessPositionReader);
}
