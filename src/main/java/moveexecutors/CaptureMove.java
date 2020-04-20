package moveexecutors;

import chess.BoardCache;
import chess.PosicionPieza;

public class CaptureMove extends SimpleMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(BoardCache boardCache) {
		super.executeMove(boardCache);
		
		boardCache.removePositions(to);
	}

	@Override
	public void undoMove(BoardCache boardCache) {
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
