package chess.board.movesgenerators.legal;

import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveKing;


/**
 * @author Mauricio Coria
 *
 */
public interface MoveFilter {

	
	boolean filterMove(Move move);
	
	
	boolean filterMove(MoveKing move) ;


	boolean filterMove(MoveCastling moveCastling);	

}
