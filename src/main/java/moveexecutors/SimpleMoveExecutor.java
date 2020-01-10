package moveexecutors;

import chess.BoardMediator;
import chess.Square;

public class SimpleMoveExecutor implements MoveExecutor{

	@Override
	public void execute(BoardMediator tablero, Square from, Square to) {
		tablero.setPieza(to, tablero.getPieza(from));
		tablero.setEmptySquare(from);
	}

}
