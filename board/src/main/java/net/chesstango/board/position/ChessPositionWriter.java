/**
 * 
 */
package net.chesstango.board.position;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionWriter {
	
	void executeMove(Move move);
	
	void executeMoveKing(MoveKing move);
	
	void undoMove(Move move);

	void undoMoveKing(MoveKing move);

}
