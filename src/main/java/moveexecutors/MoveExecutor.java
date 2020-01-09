package moveexecutors;

import chess.Board;
import chess.DummyBoard;
import chess.Square;

public interface MoveExecutor {

	void execute(Board board, Square from, Square to);
	
	void execute(DummyBoard tablero, Square from, Square to);

}
