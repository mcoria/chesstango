package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.movesgenerators.legal.MoveFilter;

/**
 * @author Mauricio Coria
 *
 */

public interface MoveCastling extends MoveKing {

	PiecePositioned getRookFrom();
	PiecePositioned getRookTo();
}
