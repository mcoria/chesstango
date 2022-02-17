/**
 * 
 */
package chess.moves;

import chess.position.imp.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */

//TODO: implement bridge pattern.
public interface MoveKing extends Move {
	
	void executeMove(KingCacheBoard kingCacheBoard);
	void undoMove(KingCacheBoard kingCacheBoard);	

}
