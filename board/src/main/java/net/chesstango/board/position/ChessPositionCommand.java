package net.chesstango.board.position;

/**
 * Represents a command capable of handling operations related to a chess move.
 * <p>
 * This interface extends multiple other command interfaces, inheriting their operations.
 * These commands include:
 * <ul>
 *     <li>{@link SquareBoardCommand} - Operations involving the chessboard and squares.</li>
 *     <li>{@link MoveCacheBoardCommand} - Handles cached move-related operations.</li>
 *     <li>{@link BitBoardCommand} - Manages low-level bitboard operations.</li>
 *     <li>{@link KingSquareCommand} - Manages KingSquare operations.</li>
 *     <li>{@link PositionStateCommand} - Tracks the position state of the chess game.</li>
 *     <li>{@link ZobristHashCommand} - Handles hashing for unique game states using Zobrist hashing.</li>
 * </ul>
 * It serves as a composite interface for managing chess moves in detail.
 *
 * @author Mauricio Coria
 */
public interface ChessPositionCommand extends
        SquareBoardCommand,
        MoveCacheBoardCommand,
        BitBoardCommand,
        KingSquareCommand,
        PositionStateCommand,
        ZobristHashCommand {
    void doMove(ChessPosition chessPosition);

    void undoMove(ChessPosition chessPosition);
}
