package net.chesstango.board.position;

/**
 * Represents a command capable of handling operations related to a chess move.
 * <p>
 * This interface extends multiple other command interfaces, inheriting their operations.
 * These commands include:
 * <ul>
 *     <li>{@link SquareBoardVisitor} - Operations involving the chessboard and squares.</li>
 *     <li>{@link MoveCacheBoardVisitor} - Handles cached move-related operations.</li>
 *     <li>{@link BitBoardVisitor} - Manages low-level bitboard operations.</li>
 *     <li>{@link KingSquareVisitor} - Manages KingSquare operations.</li>
 *     <li>{@link PositionStateVisitor} - Tracks the position state of the chess game.</li>
 *     <li>{@link ZobristHashVisitor} - Handles hashing for unique game states using Zobrist hashing.</li>
 * </ul>
 * It serves as a composite interface for managing chess moves in detail.
 *
 * @author Mauricio Coria
 */
public interface ChessPositionVisitor extends
        SquareBoardVisitor,
        MoveCacheBoardVisitor,
        BitBoardVisitor,
        KingSquareVisitor,
        PositionStateVisitor,
        ZobristHashVisitor {

    void doMove(ChessPosition chessPosition);

    void undoMove(ChessPosition chessPosition);
}
