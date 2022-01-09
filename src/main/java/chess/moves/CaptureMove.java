package chess.moves;

import chess.PiecePositioned;
import chess.Square;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PositionState;
import chess.pseudomovesfilters.MoveFilter;

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
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		
		if(to.getKey().equals(Square.a1)){
			positionState.setCastlingWhiteQueenAllowed(false);
		}
		
		if(to.getKey().equals(Square.h1)){
			positionState.setCastlingWhiteKingAllowed(false);
		}
		
		if(to.getKey().equals(Square.a8)){
			positionState.setCastlingBlackQueenAllowed(false);
		}
		
		if(to.getKey().equals(Square.h8)){
			positionState.setCastlingBlackKingAllowed(false);
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
