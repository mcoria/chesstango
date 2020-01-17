package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;

public interface MoveExecutor {

	void execute(DummyBoard board, Move move, BoardState boardState);

	void undo(DummyBoard board, Move move, BoardState boardState);

}
