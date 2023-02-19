/**
 * 
 */
package net.chesstango.board.moves;

import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPositionWriter;
import net.chesstango.board.position.imp.KingCacheBoard;

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

	default void executeMove(KingCacheBoard kingCacheBoard) {
		kingCacheBoard.setKingSquare(getFrom().getPiece().getColor(), getTo().getSquare());
	}

	default void undoMove(KingCacheBoard kingCacheBoard) {
		kingCacheBoard.setKingSquare(getFrom().getPiece().getColor(), getFrom().getSquare());
	}

}
