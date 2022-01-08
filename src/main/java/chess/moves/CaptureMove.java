package chess.moves;

import chess.Board;
import chess.BoardState;
import chess.PosicionPieza;
import chess.Square;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.pseudomovesfilters.MoveFilter;

/**
 * @author Mauricio Coria
 *
 */
class CaptureMove extends AbstractMove {
	
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
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		
		if(to.getKey().equals(Square.a1)){
			boardState.setCastleWhiteReinaPermitido(false);
		}
		
		if(to.getKey().equals(Square.h1)){
			boardState.setCastleWhiteKingPermitido(false);
		}
		
		if(to.getKey().equals(Square.a8)){
			boardState.setCastleBlackReinaPermitido(false);
		}
		
		if(to.getKey().equals(Square.h8)){
			boardState.setCastleBlackKingPermitido(false);
		}		
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