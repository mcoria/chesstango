package net.chesstango.board.position;

/**
 * Represents a command capable of handling operations related to a chess move.
 * <p>
 * This interface extends multiple other command interfaces, inheriting their operations.
 * These commands include:
 * <ul>
 *     <li>{@link ChessPositionCommand} - Operations related to the chess position state.</li>
 *     <li>{@link SquareBoardCommand} - Operations involving the chessboard and squares.</li>
 *     <li>{@link MoveCacheBoardCommand} - Handles cached move-related operations.</li>
 *     <li>{@link BitBoardCommand} - Manages low-level bitboard operations.</li>
 *     <li>{@link PositionStateCommand} - Tracks the position state of the chess game.</li>
 *     <li>{@link ZobristHashCommand} - Handles hashing for unique game states using Zobrist hashing.</li>
 * </ul>
 * It serves as a composite interface for managing chess moves in detail.
 *
 * @author Mauricio Coria
 */
public interface MoveCommand extends
        ChessPositionCommand,
        SquareBoardCommand,
        MoveCacheBoardCommand,
        BitBoardCommand,
        PositionStateCommand,
        ZobristHashCommand {
}
