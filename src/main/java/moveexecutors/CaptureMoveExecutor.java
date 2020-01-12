package moveexecutors;

import java.util.Objects;

import chess.BoardMediator;
import chess.Move;
import chess.Pieza;

public class CaptureMoveExecutor implements MoveExecutor {

	private Pieza origen;
	private Pieza capturada;

	public CaptureMoveExecutor(Pieza origen, Pieza capturada) {
		this.origen = origen;
		this.capturada = capturada;
	}
	
	@Override
	public void execute(BoardMediator board, Move move) {
		board.setPieza(move.getTo(), origen);
		board.setEmptySquare(move.getFrom());
	}

	@Override
	public void undo(BoardMediator board, Move move) {
		board.setPieza(move.getFrom(), origen);
		board.setPieza(move.getTo(), capturada);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CaptureMoveExecutor){
			CaptureMoveExecutor theOther = (CaptureMoveExecutor) obj;
			return Objects.equals(origen, theOther.origen) && Objects.equals(capturada, theOther.capturada);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return origen.hashCode();
	}	
	
	
	@Override
	public String toString() {
		return "Captura: " + origen.toString() + " - " + capturada.toString(); 
	}	
}
