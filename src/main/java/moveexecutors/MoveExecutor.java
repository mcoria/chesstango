package moveexecutors;

import chess.BoardMediator;
import chess.Square;

public interface MoveExecutor {

	void execute(BoardMediator board, Square from, Square to);

}
