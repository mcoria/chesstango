/**
 * 
 */
package chess.board.pseudomovesgenerators;

import java.util.Collection;

import chess.board.moves.MoveCastling;
import chess.board.moves.containsers.MovePair;

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
