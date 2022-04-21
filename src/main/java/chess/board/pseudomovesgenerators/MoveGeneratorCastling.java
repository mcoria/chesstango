/**
 * 
 */
package chess.board.pseudomovesgenerators;

import java.util.Collection;

import chess.board.moves.MoveCastling;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorCastling {
	
	/*
	 * Este tipo de movimientos no debe entrar en cache
	 * ver GameTest.testUndoCaptureRook()
	 */	
	Collection<MoveCastling> generateCastlingPseudoMoves();
}
