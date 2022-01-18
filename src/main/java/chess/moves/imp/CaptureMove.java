package chess.moves.imp;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPosition;
import chess.position.ColorBoard;

/**
 * @author Mauricio Coria
 *
 */
class CaptureMove extends AbstractMove {
	
	public CaptureMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
	}	
	
	@Override
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
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
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			return true;
		}
		return false;
	}

}
