package net.chesstango.board.movesgenerators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;


/**
 * @author Mauricio Coria
 *
 */
public interface MoveFilter {

	
	boolean filterMove(Move move);
	
	
	boolean filterMoveKing(MoveKing move) ;


	boolean filterMoveCastling(MoveCastling moveCastling);

}
