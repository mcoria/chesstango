package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;

public class SimpleMoveExecutor implements MoveExecutor {
	
	@Override
	public void execute(DummyBoard board, Move move, BoardState boardState) {
		board.setEmptySquare(move.getFrom().getKey());						//Dejamos origen
		board.setPieza(move.getTo().getKey(), move.getFrom().getValue());	//Vamos a destino
		
		
		boardState.setCaptura(null);
		boardState.setPeonPasanteSquare(null);
	}

	@Override
	public void undo(DummyBoard board, Move move, BoardState boardState) {
		board.setEmptySquare(move.getTo().getKey());						//Reestablecemos destino
		board.setPieza(move.getFrom());										//Volvemos a origen
	}

	/*
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SimpleMoveExecutor){
			SimpleMoveExecutor theOther = (SimpleMoveExecutor) obj;
			return Objects.equals(origen, theOther.origen);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return origen.hashCode();
	}
	
	@Override
	public String toString() {
		return "Simple: " + origen.toString();
	}*/
}
