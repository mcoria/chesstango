package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueNegroReyMoveExecutor implements MoveExecutor{

	@Override
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.e8);
		board.setEmptySquare(Square.h8);
		board.setPieza(Square.g8, Pieza.REY_NEGRO);
		board.setPieza(Square.f8, Pieza.TORRE_NEGRO);
		
		BoardState boardState = board.getBoardState();
		boardState.setFrom(from);
		boardState.setTo(to);
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(null);			
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setEmptySquare(Square.g8);
		board.setEmptySquare(Square.f8);
		board.setPieza(Square.e8, Pieza.REY_NEGRO);
		board.setPieza(Square.h8, Pieza.TORRE_NEGRO);
	}

}
