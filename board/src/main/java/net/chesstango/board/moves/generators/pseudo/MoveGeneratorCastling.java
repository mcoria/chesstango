package net.chesstango.board.moves.generators.pseudo;

import net.chesstango.board.moves.containers.MovePair;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorCastling {
	
	/**
	 * This type of moves should not be cached. See GameTest.testUndoCaptureRook().
	 */
	MovePair generateCastlingPseudoMoves();
}
