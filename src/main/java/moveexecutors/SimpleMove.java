package moveexecutors;

import chess.BoardState;
import chess.DummyBoard;
import chess.PosicionPieza;

public class SimpleMove extends AbstractMove {
	
	public SimpleMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(from.getKey());					//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue()) ;			//Vamos al destino	
	}
	
	@Override
	public void executeState(BoardState boardState) {
		super.executeState(boardState);
		boardState.setPeonPasanteSquare(null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SimpleMove";
	}
}
