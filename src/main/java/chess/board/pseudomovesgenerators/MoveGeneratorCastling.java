/**
 * 
 */
package chess.board.pseudomovesgenerators;

import chess.board.moves.containers.MovePair;

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
