package moveexecutors;

import chess.PosicionPieza;
import layers.KingCacheBoard;

public class CaptureReyMove extends CaptureMove {

	
	public CaptureReyMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(from.getValue().getColor(), to.getKey());
	}

	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(from.getValue().getColor(), from.getKey());	
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureReyMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "CaptureReyMove";
	}	
}
