package net.chesstango.board.moves;

import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 */
public interface MoveKing extends Move {

    @Override
    default void executeMove(ChessPosition chessPosition) {
        SquareBoardWriter squareBoard = chessPosition.getSquareBoard();
        BitBoardWriter bitBoard = chessPosition.getBitBoard();
        PositionStateWriter positionState = chessPosition.getPositionState();
        MoveCacheBoardWriter moveCache = chessPosition.getMoveCache();
        KingSquare kingSquare = chessPosition.getKingSquare();
        ZobristHashWriter hash = chessPosition.getZobrist();

        executeMove(squareBoard);

        executeMove(bitBoard);

        executeMove(positionState);

        executeMove(moveCache);

        executeMove(kingSquare);

        executeMove(hash, chessPosition);
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

    void executeMove(KingSquareWriter kingSquareWriter);

    void undoMove(KingSquareWriter kingSquareWriter);
}
