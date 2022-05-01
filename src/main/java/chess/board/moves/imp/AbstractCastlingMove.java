package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.position.ChessPositionWriter;
import chess.board.position.PiecePlacementWriter;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;

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
	public void executeMove(ChessPositionWriter chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPositionWriter chessPosition) {
		chessPosition.undoMove(this);
	}
	
	@Override
	public boolean filter(MoveFilter filter) {
		return filter.filterMove(this);
	}	
	
	@Override
	public void executeMove(PiecePlacementWriter board) {
		kingMove.executeMove(board);
		rookMove.executeMove(board);
	}


	@Override
	public void undoMove(PiecePlacementWriter board) {
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
		return getFrom().toString() + " " + getTo().toString() + " - " + this.getClass().getSimpleName();
	}

	@Override
	public Cardinal getMoveDirection() {
		return null;
	}
}
