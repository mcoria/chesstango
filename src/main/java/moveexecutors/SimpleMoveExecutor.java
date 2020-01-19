package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;

public class SimpleMoveExecutor implements MoveExecutor {
	
	@Override
	public void execute(DummyBoard board, BoardState boardState, Move move) {
		board.setEmptySquare(move.getFrom().getKey());						//Dejamos origen
		board.setPieza(move.getTo().getKey(), move.getFrom().getValue());	//Vamos a destino
		
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(null);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState, Move move) {
		board.setEmptySquare(move.getTo().getKey());						//Reestablecemos destino
		board.setPieza(move.getFrom());										//Volvemos a origen
	}
}
