package chess.moves;

import chess.ChessPosition;
import chess.BoardState;
import chess.Color;
import chess.PiecePositioned;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;
import chess.positioncaptures.Capturer;
import chess.pseudomovesfilters.MoveFilter;

/**
 * @author Mauricio Coria
 *
 */
abstract class CastlingMove implements Move  {
	protected final SimpleKingMove kingMove;
	protected final SimpleMove torreMove;	
	
	public CastlingMove(SimpleKingMove kingMove, SimpleMove torreMove) {
		this.kingMove = kingMove;
		this.torreMove = torreMove;
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
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeKingMove(this);
	}
	
	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoKingMove(this);
	}
	
	@Override
	//TODO: Por que no utilizar kingInCheck.getAsBoolean()
	public boolean filter(MoveFilter filter) {
		Capturer capturer = filter.getCapturer();
		Color opositeColor = kingMove.getFrom().getValue().getColor().opositeColor();
		return !capturer.positionCaptured(opositeColor, kingMove.getFrom().getKey()) // El king no esta en jaque
			&& !capturer.positionCaptured(opositeColor, torreMove.getTo().getKey()) // El king no puede ser capturado en casillero intermedio
			&& !capturer.positionCaptured(opositeColor, kingMove.getTo().getKey());  // El king no puede  ser capturado en casillero destino
	}	
	
	@Override
	public void executeMove(PiecePlacement board) {
		kingMove.executeMove(board);
		torreMove.executeMove(board);
	}


	@Override
	public void undoMove(PiecePlacement board) {
		kingMove.undoMove(board);
		torreMove.undoMove(board);
	}	
	
	@Override
	public void executeMove(BoardState boardState) {
		kingMove.executeMove(boardState);
	}

	@Override
	public void undoMove(BoardState boardState) {
		kingMove.undoMove(boardState);
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
		torreMove.executeMove(colorBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		kingMove.undoMove(colorBoard);
		torreMove.undoMove(colorBoard);
	}
	
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {	
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(kingMove.getFrom().getKey(), kingMove.getTo().getKey(), torreMove.getFrom().getKey(), torreMove.getTo().getKey(), true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(kingMove.getFrom().getKey(), kingMove.getTo().getKey(), torreMove.getFrom().getKey(), torreMove.getTo().getKey(), false);
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
