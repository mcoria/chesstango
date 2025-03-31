package net.chesstango.board.position;

/**
 * Interface representing a command that can be executed and undone on various components of a chess game.
 * This interface provides methods to perform and revert moves on different writers, such as game state,
 * chess position, square board, position state, bit board, move cache, king square, and Zobrist hash.
 *
 * @author Mauricio Coria
 */
public interface Command {
    /**
     * Executes a move on the game state.
     *
     * @param gameState the game state writer
     */
    void doMove(GameStateWriter gameState);

    /**
     * Undoes a move on the game state.
     *
     * @param gameState the game state writer
     */
    void undoMove(GameStateWriter gameState);

    /**
     * Executes a move on the chess position.
     *
     * @param chessPosition the chess position writer
     */
    void doMove(ChessPositionWriter chessPosition);

    /**
     * Undoes a move on the chess position.
     *
     * @param chessPosition the chess position writer
     */
    void undoMove(ChessPositionWriter chessPosition);

    /**
     * Executes a move on the square board.
     *
     * @param squareBoard the square board writer
     */
    void doMove(SquareBoardWriter squareBoard);

    /**
     * Undoes a move on the square board.
     *
     * @param squareBoard the square board writer
     */
    void undoMove(SquareBoardWriter squareBoard);

    /**
     * Executes a move on the position state.
     *
     * @param positionState the position state writer
     */
    void doMove(PositionStateWriter positionState);

    /**
     * Undoes a move on the position state.
     *
     * @param positionState the position state writer
     */
    void undoMove(PositionStateWriter positionState);

    /**
     * Executes a move on the bit board.
     *
     * @param bitBoard the bit board writer
     */
    void doMove(BitBoardWriter bitBoard);

    /**
     * Undoes a move on the bit board.
     *
     * @param bitBoard the bit board writer
     */
    void undoMove(BitBoardWriter bitBoard);

    /**
     * Executes a move on the move cache.
     *
     * @param moveCache the move cache writer
     */
    void doMove(MoveCacheBoardWriter moveCache);

    /**
     * Undoes a move on the move cache.
     *
     * @param moveCache the move cache writer
     */
    void undoMove(MoveCacheBoardWriter moveCache);

    /**
     * Executes a move on the king square.
     *
     * @param kingSquare the king square writer
     */
    void doMove(KingSquareWriter kingSquare);

    /**
     * Undoes a move on the king square.
     *
     * @param kingSquare the king square writer
     */
    void undoMove(KingSquareWriter kingSquare);

    /**
     * Executes a move on the Zobrist hash.
     *
     * @param hash the Zobrist hash writer
     */
    void doMove(ZobristHashWriter hash);

    /**
     * Undoes a move on the Zobrist hash.
     *
     * @param hash the Zobrist hash writer
     */
    void undoMove(ZobristHashWriter hash);
}
