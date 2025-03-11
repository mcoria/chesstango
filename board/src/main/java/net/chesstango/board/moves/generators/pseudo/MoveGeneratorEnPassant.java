package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.moves.containers.MovePair;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorEnPassant {

	/**
	 * This type of moves should not be cached.
	 * It is necessary to validate an EnPassant move as there is a possibility of check
	 * even if the pawn covering the king is not in a pinned position.
	 */
	MovePair generateEnPassantPseudoMoves();
}