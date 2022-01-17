package chess.legalmovesgenerators;

import chess.Color;
import chess.analyzer.Capturer;
import chess.moves.Move;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;


//TODO: implemetar double dispatcher para esta clase desde Move y MoveKing

/**
 * @author Mauricio Coria
 *
 */
public class MoveFilter {
	protected final PiecePlacement dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionState positionState;
	protected final Capturer capturer;
	
	public MoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState, Capturer capturer) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.capturer = capturer;
	}
	
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getTurnoActual();
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
	
	public boolean filterKingMove(Move move) {
		boolean result = false;
		
		move.executeMove(this.kingCacheBoard);

		result = filterMove(move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	public Capturer getCapturer() {
		return capturer;
	}	

}
