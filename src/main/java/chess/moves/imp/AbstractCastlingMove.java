package chess.moves.imp;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.moves.Move;
import chess.moves.MoveCastling;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacementWritter;
import chess.position.PositionState;

/**
 * @author Mauricio Coria
 *
 */
abstract class AbstractCastlingMove implements MoveCastling  {
	protected final SimpleKingMove kingMove;
	protected final SimpleMove rookMove;	
	
	public AbstractCastlingMove(SimpleKingMove kingMove, SimpleMove torreMove) {
		this.kingMove = kingMove;
		this.rookMove = torreMove;
	}

	@Override
	public PiecePositioned getFrom() {
		return kingMove.getFrom();
	}

	@Override
	public PiecePositioned getTo() {
		return kingMove.getTo();
	}
	
	@Override
	public Move getRookMove(){
		return rookMove;
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
	public boolean filter(MoveFilter filter) {
		return filter.filterMove(this);
	}	
	
	@Override
	public void executeMove(PiecePlacementWritter board) {
		kingMove.executeMove(board);
		rookMove.executeMove(board);
	}


	@Override
	public void undoMove(PiecePlacementWritter board) {
		kingMove.undoMove(board);
		rookMove.undoMove(board);
	}	
	
	@Override
	public void executeMove(PositionState positionState) {
		kingMove.executeMove(positionState);
	}

	@Override
	public void undoMove(PositionState positionState) {
		kingMove.undoMove(positionState);
	}

	@Override
	public void executeMove(KingCacheBoard kingCacheBoard) {
		kingMove.executeMove(kingCacheBoard);
	}

	@Override
	public void undoMove(KingCacheBoard kingCacheBoard) {
		kingMove.undoMove(kingCacheBoard);
		
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		kingMove.executeMove(colorBoard);
		rookMove.executeMove(colorBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		kingMove.undoMove(colorBoard);
		rookMove.undoMove(colorBoard);
	}
	
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {	
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(kingMove.getFrom().getKey(), kingMove.getTo().getKey(), rookMove.getFrom().getKey(), rookMove.getTo().getKey(), true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(kingMove.getFrom().getKey(), kingMove.getTo().getKey(), rookMove.getFrom().getKey(), rookMove.getTo().getKey(), false);
		moveCache.popCleared();
	}
	
	@Override
	public int hashCode() {
		return kingMove.hashCode();
	}

	@Override
	public int compareTo(Move theOther) {
		return kingMove.compareTo(theOther);
	}

	@Override
	public String toString() {
		return getFrom().toString() + " " + getTo().toString() + " - " + this.getClass().getSimpleName().toString();
	}
}
