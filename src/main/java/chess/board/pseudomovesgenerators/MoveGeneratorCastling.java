/**
 * 
 */
package chess.board.pseudomovesgenerators;

import java.util.Collection;

import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorCastling {
	
	/*
	 * Este tipo de movimientos no debe entrar en cache
	 * ver GameTest.testUndoCaptureRook()
	 */	
	Collection<Move> generateCastlingPseudoMoves();
}
