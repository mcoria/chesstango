package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;

public class CaptureMoveExecutor implements MoveExecutor {
	
	@Override
	public void execute(DummyBoard board, BoardState boardState, Move move) {			
		board.setEmptySquare(move.getFrom().getKey());								//Dejamos el origen
		board.setPieza(move.getTo().getKey(), move.getFrom().getValue());			//Vamos al destino	
		
		boardState.setCaptura(move.getTo());
		boardState.setPeonPasanteSquare(null);
	}

	@Override
	public void undo(DummyBoard board, BoardState boardState, Move move) {
		board.setPieza(boardState.getCaptura());								//Reestablecemos destino
		board.setPieza(move.getFrom());											//Volvemos a origen
	}

	/*
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
	*/	
}
