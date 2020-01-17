package moveexecutors;

import java.util.Objects;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;

public class SimpleMoveExecutor implements MoveExecutor {
	
	@Override
	public void execute(DummyBoard board, Move move, BoardState boardState) {
		board.setPieza(move.getTo().getKey(), move.getFrom().getValue());
		board.setEmptySquare(move.getFrom().getKey());
	}

	@Override
	public void undo(DummyBoard board, Move move, BoardState boardState) {
		board.setPieza(move.getFrom().getKey(), move.getTo().getValue());
		board.setEmptySquare(move.getTo().getKey());
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
