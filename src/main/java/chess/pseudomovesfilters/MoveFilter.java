package chess.pseudomovesfilters;

import chess.Color;
import chess.layers.ChessPositionState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.PiecePlacement;
import chess.moves.Move;
import chess.positioncaptures.Capturer;


//TODO: implemetar double dispatcher para esta clase desde Move y MoveKing

/**
 * @author Mauricio Coria
 *
 */
public class MoveFilter {
	protected final PiecePlacement dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final ChessPositionState chessPositionState;
	protected final Capturer capturer;
	
	public MoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, ChessPositionState chessPositionState, Capturer capturer) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.chessPositionState = chessPositionState;
		this.capturer = capturer;
	}
	
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = chessPositionState.getTurnoActual();
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

		result = filterMove((Move) move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	public Capturer getCapturer() {
		return capturer;
	}	

}
