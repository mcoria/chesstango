/**
 * 
 */
package net.chesstango.board.movesgenerators.legal.filters;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;

/**
 * Este filtro se utiliza cuando el jugador actual SI se encuentra en jaque
 *
 * @author Mauricio Coria
 *
 */
public class CheckMoveFilter implements MoveFilter {
	
	protected final Board dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionStateReader positionState;

	protected final FullScanSquareCaptured fullScanSquareCapturer;
	
	public CheckMoveFilter(Board dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionStateReader positionState) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.fullScanSquareCapturer = new FullScanSquareCaptured(dummyBoard);
	}
	
	@Override
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! fullScanSquareCapturer.isCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		return result;
	}	
	
	@Override
	public boolean filterMoveKing(MoveKing move) {
		boolean result = false;
		
		move.executeMove(this.kingCacheBoard);

		result = filterMove(move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	@Override
	public boolean filterMoveCastling(MoveCastling moveCastling) {
		return false;
	}		

}
