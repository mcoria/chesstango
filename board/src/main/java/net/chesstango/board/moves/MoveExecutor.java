package net.chesstango.board.moves;

import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveExecutor {

    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(ZobristHashWriter hash);
}
