package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.*;

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
		moveCache.clearPseudoMoves(kingMove.getFrom().getSquare(), kingMove.getTo().getSquare(), rookMove.getFrom().getSquare(), rookMove.getTo().getSquare(), true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(kingMove.getFrom().getSquare(), kingMove.getTo().getSquare(), rookMove.getFrom().getSquare(), rookMove.getTo().getSquare(), false);
		moveCache.popCleared();
	}

	@Override
	public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
		throw new UnsupportedOperationException("Not implemented");
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