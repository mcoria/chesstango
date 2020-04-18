package moveexecutors;

import java.util.List;

import chess.PosicionPieza;
import chess.Square;

public class CaptureMove extends SimpleMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		super.executeSquareLists(squaresTurno, squaresOpenente);
		
		squaresOpenente.remove(to.getKey());
	}

	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		super.undoSquareLists(squaresTurno, squaresOpenente);

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
