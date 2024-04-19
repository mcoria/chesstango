package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.movesgenerators.legal.MoveFilter;

/**
 * @author Mauricio Coria
 *
 */

public interface MoveCastling extends MoveKing {

	@Override
	default boolean filter(MoveFilter filter){
		return filter.filterMoveCastling(this);
	}

	PiecePositioned getRookFrom();
	PiecePositioned getRookTo();
}
