package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;

public interface MoveExecutor {

	void execute(DummyBoard board, BoardState boardState, Move move);

	void undo(DummyBoard board, BoardState boardState, Move move);

}
