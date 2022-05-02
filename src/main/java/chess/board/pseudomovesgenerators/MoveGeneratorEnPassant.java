/**
 * 
 */
package chess.board.pseudomovesgenerators;

import java.util.Collection;

import chess.board.moves.Move;
import chess.board.moves.containsers.MovePair;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveGeneratorEnPassant {

	/*
	 * Este tipo de movimientos no debe entrar en cache
	 * Es necesario validar un movimiento de EnPassant dado que hay posibilidad de jaque
	 * por mas que el peon que cubra al rey no sea pinned position 
	 */
	MovePair generateEnPassantPseudoMoves();

}