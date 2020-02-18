package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class SimpleMoveExecutor implements MoveExecutor {
	
	@Override
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		board.setEmptySquare(from.getKey());					//Dejamos origen
		board.setPieza(to.getKey(), from.getValue()) ;			//Vamos a destino
		
		BoardState boardState = board.getBoardState();
		boardState.setFrom(from);
		boardState.setTo(to);
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(null);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setPosicion(boardState.getTo());					//Reestablecemos destino
		board.setPosicion(boardState.getFrom());				//Volvemos a origen
	}
}
