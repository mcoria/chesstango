/**
 * 
 */
package chess.board.moves;

import chess.board.Piece;

/**
 * @author Mauricio Coria
 *
 */
public interface MovePromotion extends Move {
	Piece getPromotion();
}
