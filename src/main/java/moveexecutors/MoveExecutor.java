package moveexecutors;

import chess.DummyBoard;
import chess.Move;

public interface MoveExecutor {

	void execute(DummyBoard board, Move move);

	void undo(DummyBoard board, Move move);

}
