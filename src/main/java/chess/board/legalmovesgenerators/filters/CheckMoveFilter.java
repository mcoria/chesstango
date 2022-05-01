/**
 * 
 */
package chess.board.legalmovesgenerators.filters;

import chess.board.Color;
import chess.board.legalmovesgenerators.squarecapturers.FullScanSquareCapturer;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveKing;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;

/**
 * Este filtro se utiliza cuando el jugador actual SI se encuentra en jaque
 *
 * @author Mauricio Coria
 *
 */
public class CheckMoveFilter implements MoveFilter {
	
	protected final PiecePlacement dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionState positionState;

	protected final FullScanSquareCapturer fullScanSquareCapturer;
	
	public CheckMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.fullScanSquareCapturer = new FullScanSquareCapturer(dummyBoard);
	}
	
	@Override
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getTurnoActual();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! fullScanSquareCapturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		return result;
	}	
	
	@Override
	public boolean filterMove(MoveKing move) {
		boolean result = false;
		
		move.executeMove(this.kingCacheBoard);

		result = filterMove((Move)move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	@Override
	public boolean filterMove(MoveCastling moveCastling) {
		return false;
	}		

}
