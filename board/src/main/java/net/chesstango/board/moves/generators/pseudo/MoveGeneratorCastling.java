package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.moves.containers.MovePair;
import net.chesstango.board.moves.PseudoMove;

/**
 * Interface for generating castling pseudo-moves in a chess game.
 * Implementations of this interface should provide a method to generate
 * pseudo-moves for castling. These moves should not be cached.
 *
 * @see MovePair
 * @see PseudoMove
 *
 * @author Mauricio Coria
 */
public interface MoveGeneratorCastling {

	/**
	 * Generates pseudo-moves for castling.
	 * This type of moves should not be cached. See GameTest.testUndoCaptureRook().
	 *
	 * @return a pair of pseudo-moves for castling
	 */
	MovePair<PseudoMove> generateCastlingPseudoMoves();
}
