package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public interface MoveExecutor {

	void execute(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to);

	void undo(DummyBoard board, BoardState boardState);

}
