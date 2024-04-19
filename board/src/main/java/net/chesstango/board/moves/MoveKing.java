package net.chesstango.board.moves;

import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveKing extends Move {

    /**
     * This method checks if this move is legal or not.
     *
     * @param filter
     * @return
     */
    @Override
    default boolean isLegalMove(MoveFilter filter) {
        return filter.isLegalMoveKing(this);
    }

    @Override
    default void doMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        KingSquare kingSquare = chessPosition.getKingSquare();
        ZobristHashWriter hash = chessPosition.getZobrist();

        doMove(squareBoard);

        doMove(bitBoard);

        doMove(positionState);

        doMove(moveCache);

        doMove(kingSquare);

        doMove(hash, chessPosition);
    }

    @Override
    default void undoMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        KingSquare kingSquare = chessPosition.getKingSquare();
        ZobristHashWriter hash = chessPosition.getZobrist();

        undoMove(squareBoard);

        undoMove(bitBoard);

        undoMove(positionState);

        undoMove(moveCache);

        undoMove(kingSquare);

        undoMove(hash);
    }

    void doMove(KingSquareWriter kingSquareWriter);

    void undoMove(KingSquareWriter kingSquareWriter);
}
