package moveexecutors;

import chess.PosicionPieza;
import layers.ColorBoard;
import layers.DummyBoard;
import layers.MoveCacheBoard;

public abstract class EnroqueMove extends AbstractMove {
	
	protected abstract SimpleReyMove getReyMove();
	protected abstract SimpleMove getTorreMove();
	
	public EnroqueMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		getReyMove().executeMove(board);
		getTorreMove().executeMove(board);
	}


	@Override
	public void undoMove(DummyBoard board) {
		getReyMove().undoMove(board);
		getTorreMove().undoMove(board);
	}	
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		getReyMove().executeMove(colorBoard);
		getTorreMove().executeMove(colorBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		getReyMove().undoMove(colorBoard);
		getTorreMove().undoMove(colorBoard);
	}	
	
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushState();
		moveCache.clearPseudoMoves(getReyMove().getFrom().getKey(), getReyMove().getTo().getKey(), getTorreMove().getFrom().getKey(), getTorreMove().getTo().getKey());
	}	

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnroqueMove){
			EnroqueMove theOther = (EnroqueMove) obj;
			return getFrom().equals(theOther.getFrom()) &&  getTo().equals(theOther.getTo());
		}
		return false;
	}

	@Override
	public String toString() {
		return getFrom().toString() + " " + getTo().toString() + "; " + getType();
	}

	protected abstract String getType();
}
