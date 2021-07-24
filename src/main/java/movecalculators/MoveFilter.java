package movecalculators;

import chess.BoardState;
import chess.Color;
import chess.KingMove;
import chess.Move;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import positioncaptures.Capturer;


//TODO: implemetar double dispatcher para esta clase desde Move y MoveKing
public class MoveFilter {
	protected final PosicionPiezaBoard dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final BoardState boardState;
	protected final Capturer capturer;
	
	public MoveFilter(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, BoardState boardState, Capturer capturer) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.boardState = boardState;
		this.capturer = capturer;
	}
	
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = boardState.getTurnoActual();
		final Color opositeTurnoActual = turnoActual.opositeColor();
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! capturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		return result;
	}
	
	public boolean filterMove(KingMove move) {
		boolean result = false;
		
		move.executeMove(this.kingCacheBoard);

		result = filterMove((Move) move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}	

}
