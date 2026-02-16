package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.PositionWriter;
import net.chesstango.board.representations.move.SimpleMoveEncoder;

import java.io.Serializable;


/**
 * Interface representing a move in a chess game.
 * <p>
 * A Move represents a single chess move operation that can be executed and undone on a chess board.
 * This interface provides methods to get the starting and ending positions of the move,
 * execute and undo the move, and get various properties of the move such as its direction,
 * whether it is quiet, and its Zobrist hash.
 * <p>
 * The Move interface serves as the fundamental unit of chess game operations, encapsulating
 * all information necessary to perform and reverse a move on the board. Implementations of this
 * interface handle various types of moves including simple moves, captures, castling, en passant,
 * and pawn promotions.
 * <p>
 * <b>Key Concepts:</b>
 * <ul>
 *   <li><b>Execution and Undo:</b> Moves can be executed on the board state via {@link #executeMove()}
 *       and reversed via {@link #undoMove()}, enabling move search algorithms to explore game trees
 *       efficiently without copying board state.</li>
 *   <li><b>Quiet Moves:</b> A quiet move is one that does not capture a piece or give check.
 *       This distinction is important for search optimization and move ordering heuristics.</li>
 *   <li><b>Zobrist Hashing:</b> Each move provides a Zobrist hash value via {@link #getZobristHash()}
 *       that enables incremental hash updates for position identification and transposition table usage.</li>
 *   <li><b>Move Encoding:</b> Moves can be encoded in multiple formats including binary encoding
 *       for compact storage and coordinate notation for human readability and UCI protocol compliance.</li>
 * </ul>
 * <p>
 * <b>Usage Pattern:</b>
 * <pre>{@code
 * // Execute a move
 * move.executeMove();
 *
 * // Perform analysis or search
 * int evaluation = evaluatePosition();
 *
 * // Undo the move to restore previous state
 * move.undoMove();
 * }</pre>
 * <p>
 * The binaryEncoding method provides a bit field representation of the move for efficient storage
 * and transmission. The coordinate encoding provides a human-readable string representation compatible
 * with standard chess notation systems.
 * <p>
 * <b>Thread Safety:</b> Move objects are not thread-safe. Each move should only be executed and undone
 * sequentially within a single thread context.
 *
 * @author Mauricio Coria
 * @see net.chesstango.board.PiecePositioned
 * @see net.chesstango.board.Square
 * @see net.chesstango.board.iterators.Cardinal
 * @see PositionWriter
 */
public interface Move extends Serializable {

    /**
     * Gets the starting position of the move.
     *
     * @return the starting position of the move
     */
    PiecePositioned getFrom();

    /**
     * Gets the ending position of the move.
     *
     * @return the ending position of the move
     */
    PiecePositioned getTo();

    /**
     * Executes the move.
     */
    void executeMove();

    /**
     * Undoes the move.
     */
    void undoMove();

    /**
     * Provides a bit field representation of the move.
     * <p>
     * "move" is a bit field with the following meaning (bit 0 is the least significant bit):
     * bits                meaning
     * ===================================
     * 0,1,2               to file
     * 3,4,5               to row
     * 6,7,8               from file
     * 9,10,11             from row
     * 12,13,14            promotion piece
     * <p>
     * "promotion piece" is encoded as follows:
     * none       0
     * knight     1
     * bishop     2
     * rook       3
     * queen      4
     *
     * @return a short representing the bit field encoding of the move
     */
    default short binaryEncoding() {
        Square fromSquare = getFrom().square();
        Square toSquare = getTo().square();
        return (short) (fromSquare.getBinaryEncodedFrom() | toSquare.getBinaryEncodedTo());
    }


    /**
     * Returns a string representation of the move in coordinate notation.
     * The format is the starting square followed by the ending square,
     * with an optional promotion piece indicator (e.g., "e2e4" or "e7e8q").
     *
     * @return a string representing the move in coordinate notation
     */
    default String coordinateEncoding() {
        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
        return simpleMoveEncoder.encode(this);
    }

    /**
     * Gets the direction of the move.
     *
     * @return the direction of the move
     */
    Cardinal getMoveDirection();

    /**
     * Checks if the move is quiet.
     *
     * @return true if the move is quiet, false otherwise
     */
    boolean isQuiet();

    /**
     * Gets the Zobrist hash of the move.
     *
     * @return the Zobrist hash of the move
     */
    long getZobristHash();
}