package chess.moves;

import chess.Board;
import chess.PiecePositioned;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
class SimpleMove extends AbstractMove {
	
	public SimpleMove(PiecePositioned from, PiecePositioned to) {
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
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}
	
	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
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
		if(super.equals(obj)  && obj instanceof SimpleMove){
			return true;
		}
		return false;
	}
}
