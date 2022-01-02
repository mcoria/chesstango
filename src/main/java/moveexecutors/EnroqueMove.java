package moveexecutors;

import chess.Board;
import chess.BoardState;
import chess.Color;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.MoveFilter;
import positioncaptures.Capturer;

/**
 * @author Mauricio Coria
 *
 */
abstract class EnroqueMove implements Move  {
	protected final SimpleReyMove reyMove;
	protected final SimpleMove torreMove;	
	
	public EnroqueMove(SimpleReyMove reyMove, SimpleMove torreMove) {
		this.reyMove = reyMove;
		this.torreMove = torreMove;
	}

	@Override
	public PosicionPieza getFrom() {
		return reyMove.getFrom();
	}

	@Override
	public PosicionPieza getTo() {
		return reyMove.getTo();
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
	//TODO: Por que no utilizar kingInCheck.getAsBoolean()
	public boolean filter(MoveFilter filter) {
		Capturer capturer = filter.getCapturer();
		Color opositeColor = reyMove.getFrom().getValue().getColor().opositeColor();
		return !capturer.positionCaptured(opositeColor, reyMove.getFrom().getKey()) // El rey no esta en jaque
			&& !capturer.positionCaptured(opositeColor, torreMove.getTo().getKey()) // El rey no puede ser capturado en casillero intermedio
			&& !capturer.positionCaptured(opositeColor, reyMove.getTo().getKey());  // El rey no puede  ser capturado en casillero destino
	}	
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		reyMove.executeMove(board);
		torreMove.executeMove(board);
	}


	@Override
	public void undoMove(PosicionPiezaBoard board) {
		reyMove.undoMove(board);
		torreMove.undoMove(board);
	}	
	
	@Override
	public void executeMove(BoardState boardState) {
		reyMove.executeMove(boardState);
	}

	@Override
	public void undoMove(BoardState boardState) {
		reyMove.undoMove(boardState);
	}

	@Override
	public void executeMove(KingCacheBoard kingCacheBoard) {
		reyMove.executeMove(kingCacheBoard);
	}

	@Override
	public void undoMove(KingCacheBoard kingCacheBoard) {
		reyMove.undoMove(kingCacheBoard);
		
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		reyMove.executeMove(colorBoard);
		torreMove.executeMove(colorBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		reyMove.undoMove(colorBoard);
		torreMove.undoMove(colorBoard);
	}
	
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {	
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(reyMove.getFrom().getKey(), reyMove.getTo().getKey(), torreMove.getFrom().getKey(), torreMove.getTo().getKey(), true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(reyMove.getFrom().getKey(), reyMove.getTo().getKey(), torreMove.getFrom().getKey(), torreMove.getTo().getKey(), false);
		moveCache.popCleared();
	}
	
	@Override
	public int hashCode() {
		return reyMove.hashCode();
	}

	@Override
	public int compareTo(Move theOther) {
		return reyMove.compareTo(theOther);
	}

	@Override
	public String toString() {
		return getFrom().toString() + " " + getTo().toString() + " - " + this.getClass().getSimpleName().toString();
	}
}
