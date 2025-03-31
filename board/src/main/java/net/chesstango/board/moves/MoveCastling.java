package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;

/**
 * Interface representing a castling move in a chess game.
 * This interface extends the Move interface and provides additional methods
 * to get the starting and ending positions of the rook involved in the castling move.
 *
 * @see Move
 * @see PiecePositioned
 * @see net.chesstango.board.Square
 *
 * @author Mauricio Coria
 */
public interface MoveCastling extends Move {

    /**
     * Gets the starting position of the rook involved in the castling move.
     *
     * @return the starting position of the rook
     */
    PiecePositioned getRookFrom();

    /**
     * Gets the ending position of the rook involved in the castling move.
     *
     * @return the ending position of the rook
     */
    PiecePositioned getRookTo();
}
