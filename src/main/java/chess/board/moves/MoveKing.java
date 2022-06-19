/**
 * 
 */
package chess.board.moves;

import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */

//TODO: implement bridge pattern.
public interface MoveKing extends Move {

	default void executeMove(ChessPositionWriter chessPosition){
		chessPosition.executeMove(this);
	}
	default void undoMove(ChessPositionWriter chessPosition){
		chessPosition.undoMove(this);
	}
	default boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}

	void executeMove(KingCacheBoard kingCacheBoard);
	void undoMove(KingCacheBoard kingCacheBoard);	

}
