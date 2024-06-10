package net.chesstango.board.moves;

import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveExecutor {
    void doMove(ChessPosition chessPosition);

    void undoMove(ChessPosition chessPosition);

    void doMove(SquareBoardWriter squareBoard);

    void undoMove(SquareBoardWriter squareBoard);

    void doMove(PositionStateWriter positionState);

    void undoMove(PositionStateWriter positionStateWriter);

    void doMove(BitBoardWriter bitBoard);

    void undoMove(BitBoardWriter bitBoard);

    void doMove(MoveCacheBoardWriter moveCache);

    void undoMove(MoveCacheBoardWriter moveCache);

    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(ZobristHashWriter hash);
}
