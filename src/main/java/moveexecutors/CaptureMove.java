package moveexecutors;

import chess.BoardCache;
import chess.DummyBoard;
import chess.PosicionPieza;

public class CaptureMove extends SimpleMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.move(from, to);
	}
	
	@Override
	public void executeMove(BoardCache boardCache) {
		super.executeMove(boardCache);
		
		boardCache.removePositions(to.getValue().getColor(), to.getKey());
	}

	@Override
	public void undoMove(BoardCache boardCache) {
		super.undoMove(boardCache);

		boardCache.addPositions(to.getValue().getColor(), to.getKey());
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
