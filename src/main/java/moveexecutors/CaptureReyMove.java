package moveexecutors;

import chess.BoardCache;
import chess.Color;
import chess.PosicionPieza;
import chess.MoveKing;

public class CaptureReyMove extends CaptureMove implements MoveKing {

	
	public CaptureReyMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}


	@Override
	public void executetSquareKingCache(BoardCache cache) {
		if(Color.BLANCO.equals(from.getValue().getColor())){
			cache.setSquareKingBlancoCache(to.getKey());
		} else {
			cache.setSquareKingNegroCache(to.getKey());
		}
	}

	@Override
	public void undoSquareKingCache(BoardCache cache) {
		if(Color.BLANCO.equals(from.getValue().getColor())){
			cache.setSquareKingBlancoCache(from.getKey());
		} else {
			cache.setSquareKingNegroCache(from.getKey());
		}		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureReyMove){
			return true;
		}
		return false;
	}	
}