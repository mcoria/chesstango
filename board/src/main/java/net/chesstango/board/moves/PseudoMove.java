package net.chesstango.board.moves;

import net.chesstango.board.moves.generators.legal.LegalMoveFilter;

/**
 * Interface representing a pseudo move in a chess game.
 * A pseudo move is a move that may or may not be legal depending on the game rules.
 * This interface extends the Move interface and adds a method to check the legality of the move.
 *
 * @author Mauricio Coria
 */
public interface PseudoMove extends Move {

    /**
     * Checks if the move is legal based on the provided filter.
     *
     * @param filter the LegalMoveFilter used to determine if the move is legal
     * @return true if the move is legal, false otherwise
     */
    boolean isLegalMove(LegalMoveFilter filter);
}
