package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueBlancoReynaMoveExecutor implements MoveExecutor{

	@Override
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.e1);
		board.setEmptySquare(Square.a1);
		board.setPieza(Square.c1, Pieza.REY_BLANCO);
		board.setPieza(Square.d1, Pieza.TORRE_BLANCO);
		
		BoardState boardState = board.getBoardState();
		boardState.setFrom(from);
		boardState.setTo(to);		
		boardState.setEnroqueBlancoReyPermitido(false);
		boardState.setEnroqueBlancoReinaPermitido(false);
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(null);	
	}

	@Override
	public void undo(DummyBoard board) {
		board.setEmptySquare(Square.c1);
		board.setEmptySquare(Square.d1);
		board.setPieza(Square.e1, Pieza.REY_BLANCO);
		board.setPieza(Square.a1, Pieza.TORRE_BLANCO);
	}

}
