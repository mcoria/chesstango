package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.position.ZobristHashWriter;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
interface ZobristExecutor {
    void apply(PiecePositioned from, PiecePositioned to, ZobristHashWriter hash, ChessPositionReader chessPositionReader);
}
