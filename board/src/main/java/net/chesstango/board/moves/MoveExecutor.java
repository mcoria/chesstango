package net.chesstango.board.moves;

import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveExecutor {

    void doMove(SquareBoardWriter squareBoard);

    void undoMove(SquareBoardWriter squareBoard);

    void doMove(PositionStateWriter positionState);

    void undoMove(PositionStateWriter positionStateWriter);

    void doMove(BitBoardWriter bitBoard);

    void undoMove(BitBoardWriter bitBoard);

    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(ZobristHashWriter hash);
}
