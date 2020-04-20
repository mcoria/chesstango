package moveexecutors;

import java.util.List;

import chess.PosicionPieza;
import chess.Square;

public class CaptureMove extends SimpleMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(List<Square> squaresTurno, List<Square> squaresOpenente) {
		super.executeMove(squaresTurno, squaresOpenente);
		
		squaresOpenente.remove(to.getKey());
	}

	@Override
	public void undoMove(List<Square> squaresTurno, List<Square> squaresOpenente) {
		super.undoMove(squaresTurno, squaresOpenente);

		squaresOpenente.add(to.getKey());
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
