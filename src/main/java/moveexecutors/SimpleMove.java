package moveexecutors;

import chess.DummyBoard;
import chess.PosicionPieza;

public class SimpleMove extends AbstractMove {
	
	public SimpleMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.move(from, to.getKey());
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
