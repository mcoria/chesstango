package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueBlancoReyMoveExecutor implements MoveExecutor{

	@Override
	public void execute(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.e1);
		board.setEmptySquare(Square.h1);
		board.setPieza(Square.g1, Pieza.REY_BLANCO);
		board.setPieza(Square.f1, Pieza.TORRE_BLANCO);
		
		boardState.setFrom(from);
		boardState.setTo(to);		
		boardState.setEnroqueBlancoReyPermitido(false);
		boardState.setEnroqueBlancoReinaPermitido(false);
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(null);		
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setEmptySquare(Square.g1);
		board.setEmptySquare(Square.f1);
		board.setPieza(Square.e1, Pieza.REY_BLANCO);
		board.setPieza(Square.h1, Pieza.TORRE_BLANCO);
	}

}
