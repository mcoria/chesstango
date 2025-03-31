package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.PseudoMove;

/**
 * This interface defines the contract for generating pseudo-legal En Passant moves
 * in a chess game. En Passant is a special pawn capture move that requires validation
 * as it can lead to a potential check scenario, even if the pawn is not pinned.
 * Implementations of this interface are responsible for creating these moves dynamically
 * since they should not be cached given their unique validation requirements.
 *
 * @author Mauricio Coria
 */
public interface MoveGeneratorEnPassant {

    /**
     * This type of moves should not be cached.
     * It is necessary to validate an EnPassant move as there is a possibility of check
     * even if the pawn covering the king is not in a pinned position.
     */
    MovePair<PseudoMove> generateEnPassantPseudoMoves();
}