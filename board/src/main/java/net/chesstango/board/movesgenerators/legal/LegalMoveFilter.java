package net.chesstango.board.movesgenerators.legal;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;


/**
 * @author Mauricio Coria
 *
 */
public interface LegalMoveFilter {

	
	boolean isLegalMove(Move move);
	
	
	boolean isLegalMove(MoveKing move) ;


	boolean isLegalMove(MoveCastling moveCastling);

}
