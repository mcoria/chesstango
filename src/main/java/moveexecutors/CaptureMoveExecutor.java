package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class CaptureMoveExecutor implements MoveExecutor {
	
	@Override
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {			
		board.setEmptySquare(from.getKey());								//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue());						//Vamos al destino	
		
		BoardState boardState = board.getBoardState();
		boardState.setFrom(from);
		boardState.setTo(to);		
		boardState.setCaptura(to);
		boardState.setPeonPasanteSquare(null);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState) {
		board.setPosicion(boardState.getCaptura());							//Reestablecemos destino
		board.setPosicion(boardState.getFrom());							//Volvemos a origen
	}
}
