/**
 * 
 */
package chess.position;

import chess.moves.Move;
import chess.moves.MoveKing;

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
