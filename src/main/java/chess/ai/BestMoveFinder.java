package chess.ai;

import chess.board.Game;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface BestMoveFinder {
	
	Move findBestMove(Game game);

}
