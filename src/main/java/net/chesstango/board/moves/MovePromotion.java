/**
 * 
 */
package net.chesstango.board.moves;

import net.chesstango.board.Piece;

/**
 * @author Mauricio Coria
 *
 */
public interface MovePromotion extends Move {
	Piece getPromotion();
}
