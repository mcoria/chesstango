package net.chesstango.board.moves;

import net.chesstango.board.Piece;

/**
 * Interface representing a promotion move in a chess game.
 * This interface extends the Move interface and provides an additional method
 * to get the piece that the pawn is promoted to.
 *
 * @see Move
 * @see Piece
 *
 * @author Mauricio Coria
 */
public interface MovePromotion extends Move {

    /**
     * Gets the piece that the pawn is promoted to.
     *
     * @return the promotion piece
     */
    Piece getPromotion();
}
