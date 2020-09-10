package moveexecutors;

import chess.PosicionPieza;
import layers.ColorBoard;

public class CaptureMove extends SimpleMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(ColorBoard boardCache) {
		super.executeMove(boardCache);
		
		boardCache.removePositions(to);
	}

	@Override
	public void undoMove(ColorBoard boardCache) {
		super.undoMove(boardCache);

		boardCache.addPositions(to);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "CaptureMove";
	}

}
