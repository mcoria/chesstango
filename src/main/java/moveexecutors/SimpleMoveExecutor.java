package moveexecutors;

import java.util.Objects;

import chess.BoardMediator;
import chess.Move;
import chess.Pieza;

public class SimpleMoveExecutor implements MoveExecutor {
	
	private Pieza origen;

	public SimpleMoveExecutor(Pieza origen) {
		this.origen = origen;
	}
	
	@Override
	public void execute(BoardMediator board, Move move) {
		board.setPieza(move.getTo(), origen);
		board.setEmptySquare(move.getFrom());
	}

	@Override
	public void undo(BoardMediator board, Move move) {
		board.setPieza(move.getFrom(), origen);
		board.setEmptySquare(move.getTo());
	}

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
		// TODO Auto-generated method stub
		return origen.hashCode();
	}
	
	@Override
	public String toString() {
		return "Simple: " + origen.toString();
	}
}
