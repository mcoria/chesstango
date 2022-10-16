/**
 * 
 */
package net.chesstango.board.movesgenerators.pseudo;

import net.chesstango.board.moves.containers.MovePair;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorCastling {
	
	/*
	 * Este tipo de movimientos no debe entrar en cache
	 * ver GameTest.testUndoCaptureRook()
	 */
	MovePair generateCastlingPseudoMoves();
}
