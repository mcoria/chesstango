package moveexecutors;

import chess.Color;
import chess.PosicionPieza;
import layers.ColorBoard;

public class CaptureReyMove extends CaptureMove {

	
	public CaptureReyMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}


	@Override
	public void executeMove(ColorBoard cache) {
		super.executeMove(cache);
		if(Color.BLANCO.equals(from.getValue().getColor())){
			cache.setSquareKingBlancoCache(to.getKey());
		} else {
			cache.setSquareKingNegroCache(to.getKey());
		}
	}

	@Override
	public void undoMove(ColorBoard cache) {
		super.undoMove(cache);
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
	
	@Override
	protected String getType() {
		return "CaptureReyMove";
	}	
}
