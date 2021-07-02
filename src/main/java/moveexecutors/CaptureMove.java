package moveexecutors;

import chess.PosicionPieza;
import layers.ColorBoard;

public class CaptureMove extends SimpleMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		super.executeMove(colorBoard);
		
		colorBoard.removePositions(to);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		super.undoMove(colorBoard);

		colorBoard.addPositions(to);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			return true;
		}
		return false;
	}

}
