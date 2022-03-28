package chess.moves.imp;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPositionWriter;
import chess.position.imp.ColorBoard;


/**
 * @author Mauricio Coria
 *
 */
class SimpleMove extends AbstractMove {
	
	public SimpleMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(ChessPositionWriter chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPositionWriter chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}
	
	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
	}	
	

	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleMove){
			return true;
		}
		return false;
	}
}
