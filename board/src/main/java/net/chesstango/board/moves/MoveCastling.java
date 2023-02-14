/**
 * 
 */
package net.chesstango.board.moves;

import net.chesstango.board.movesgenerators.legal.MoveFilter;

/**
 * @author Mauricio Coria
 *
 */

//TODO: implement bridge pattern.
public interface MoveCastling extends MoveKing {

	default boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}

	Move getRookMove();
}