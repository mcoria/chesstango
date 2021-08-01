package moveexecutors;

import chess.Board;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.MoveFilter;

public class CaptureMove extends AbstractMove {
	
	public CaptureMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}	
	
	@Override
	public void executeMove(Board board) {
		board.executeMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}	
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		board.move(from, to);
	}
	
	@Override
	public void undoMove(PosicionPiezaBoard board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}

	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.removePositions(to);
		
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}
	
	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
		
		colorBoard.addPositions(to);
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard) {
		throw new RuntimeException("Error !");
	}

	@Override
	public void undoMove(KingCacheBoard kingCacheBoard) {
		throw new RuntimeException("Error !");
	}		
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			return true;
		}
		return false;
	}

}
