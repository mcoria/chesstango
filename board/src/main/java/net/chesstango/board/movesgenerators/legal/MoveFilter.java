package net.chesstango.board.movesgenerators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;


/**
 * @author Mauricio Coria
 *
 */
public interface MoveFilter {

	
	boolean isLegalMove(Move move);
	
	
	boolean isLegalMoveKing(MoveKing move) ;


	boolean isLegalMoveCastling(MoveCastling moveCastling);

}
