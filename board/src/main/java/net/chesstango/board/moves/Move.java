package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.PositionWriter;


/**
 * Interface representing a move in a chess game.
 * This interface provides methods to get the starting and ending positions of the move,
 * execute and undo the move, and get various properties of the move such as its direction,
 * whether it is quiet, and its Zobrist hash.
 * <p>
 * The binaryEncoding method provides a bit field representation of the move.
 *
 * @see net.chesstango.board.PiecePositioned
 * @see net.chesstango.board.Square
 * @see net.chesstango.board.iterators.Cardinal
 * @see PositionWriter
 *
 * @author Mauricio Coria
 */
public interface Move {

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
        Square fromSquare = getFrom().getSquare();
        Square toSquare = getTo().getSquare();
        return (short) (fromSquare.getBinaryEncodedFrom() | toSquare.getBinaryEncodedTo());
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