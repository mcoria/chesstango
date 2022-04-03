/**
 * 
 */
package chess.board.position;

import chess.board.moves.Move;
import chess.board.moves.MoveKing;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionWriter {
	
	void executeMove(Move move);
	
	void executeMove(MoveKing move);
	
	void undoMove(Move move);

	void undoMove(MoveKing move);

}
