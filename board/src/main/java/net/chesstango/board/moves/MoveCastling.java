package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.movesgenerators.legal.LegalMoveFilter;

/**
 * @author Mauricio Coria
 *
 */

public interface MoveCastling extends MoveKing {

	PiecePositioned getRookFrom();
	PiecePositioned getRookTo();

	/**
	 * This method checks if this move is legal or not.
	 *
	 * @param filter
	 * @return
	 */
	@Override
	default boolean isLegalMove(LegalMoveFilter filter){
		return filter.isLegalMove(this);
	}

}
