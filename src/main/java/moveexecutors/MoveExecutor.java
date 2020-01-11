package moveexecutors;

import chess.BoardMediator;
import chess.Move;

public interface MoveExecutor {

	void execute(BoardMediator board, Move move);

	void undo(BoardMediator board, Move move);

}
