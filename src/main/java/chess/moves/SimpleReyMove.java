package chess.moves;

import chess.Board;
import chess.PiecePositioned;
import chess.layers.KingCacheBoard;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
class SimpleKingMove extends SimpleMove {

	public SimpleKingMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(Board board) {
		board.executeKingMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoKingMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterKingMove(this);
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getFrom().getKey());	
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleKingMove){
			return true;
		}
		return false;
	}

}
