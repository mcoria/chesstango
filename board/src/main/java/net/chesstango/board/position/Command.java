package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface Command {
    void doMove(GameStateWriter gameState);
    void undoMove(GameStateWriter gameState);

    void doMove(ChessPositionWriter chessPosition);
    void undoMove(ChessPositionWriter chessPosition);

    void doMove(SquareBoardWriter squareBoard);
    void undoMove(SquareBoardWriter squareBoard);

    void doMove(PositionStateWriter positionState);
    void undoMove(PositionStateWriter positionState);

    void doMove(BitBoardWriter bitBoard);
    void undoMove(BitBoardWriter bitBoard);

    void doMove(MoveCacheBoardWriter moveCache);
    void undoMove(MoveCacheBoardWriter moveCache);

    void doMove(KingSquareWriter kingSquare);
    void undoMove(KingSquareWriter kingSquare);

    void doMove(ZobristHashWriter hash);
    void undoMove(ZobristHashWriter hash);
}
