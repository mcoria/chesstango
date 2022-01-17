/**
 * 
 */
package chess.moves;

import chess.position.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveKing extends Move {
	
	void executeMove(KingCacheBoard kingCacheBoard);
	void undoMove(KingCacheBoard kingCacheBoard);	

}
