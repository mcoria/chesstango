package moveexecutors;

import chess.Board;
import chess.DummyBoard;
import chess.Square;

public class SimpleMoveExecutor implements MoveExecutor{

	@Override
	public void execute(Board board, Square from, Square to) {
		execute(board.getTablero(), from, to);
	}

	@Override
	public void execute(DummyBoard tablero, Square from, Square to) {
		tablero.setPieza(to, tablero.getPieza(from));
		tablero.setEmptySquare(from);
	}

}
