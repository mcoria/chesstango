package moveexecutors;

import chess.PosicionPieza;

public class CaptureReyMove extends SimpleReyMove {

	
	public CaptureReyMove(PosicionPieza from, PosicionPieza to) {
		super(new CaptureMove(from, to));
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureReyMove){
			return true;
		}
		return false;
	}

}
