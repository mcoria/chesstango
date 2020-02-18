package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class SaltoDoblePeonMoveExecutor implements MoveExecutor {

	@Override
	public void execute(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		Square peonPasanteSquare = Square.getSquare(to.getKey().getFile(),  Color.BLANCO.equals(from.getValue().getColor()) ? to.getKey().getRank() - 1 : to.getKey().getRank() + 1);
		board.setEmptySquare(from.getKey());								//Dejamos origen
		board.setPieza(to.getKey(), from.getValue());						//Vamos a destino
		
		boardState.setFrom(from);
		boardState.setTo(to);		
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(peonPasanteSquare);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setPosicion(boardState.getTo());					//Reestablecemos destino
		board.setPosicion(boardState.getFrom());				//Volvemos a origen
	}

}
