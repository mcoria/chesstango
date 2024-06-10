package net.chesstango.board.moves;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;

/**
 * @author Mauricio Coria
 *
 */

public interface MoveCastling extends MoveKing {

	PiecePositioned getRookFrom();
	PiecePositioned getRookTo();

}
