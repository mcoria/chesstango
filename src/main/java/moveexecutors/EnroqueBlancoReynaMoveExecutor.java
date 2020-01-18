package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public class EnroqueBlancoReynaMoveExecutor implements MoveExecutor{

	@Override
	public void execute(DummyBoard board, BoardState boardState, Move move) {
		board.setEmptySquare(Square.e1);
		board.setEmptySquare(Square.a1);
		board.setPieza(Square.c1, Pieza.REY_BLANCO);
		board.setPieza(Square.d1, Pieza.TORRE_BLANCO);
		
		boardState.setEnroqueBlancoReinaPermitido(false);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState, Move move) {
		board.setEmptySquare(Square.c1);
		board.setEmptySquare(Square.d1);
		board.setPieza(Square.e1, Pieza.REY_BLANCO);
		board.setPieza(Square.a1, Pieza.TORRE_BLANCO);
	}

}
