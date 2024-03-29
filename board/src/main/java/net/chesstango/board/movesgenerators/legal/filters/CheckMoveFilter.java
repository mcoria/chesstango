package net.chesstango.board.movesgenerators.legal.filters;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.imp.KingSquareImp;

/**
 * Este filtro se utiliza cuando el jugador actual SI se encuentra en jaque
 *
 * @author Mauricio Coria
 *
 */
public class CheckMoveFilter implements MoveFilter {
	
	protected final SquareBoard dummySquareBoard;
	protected final KingSquareImp kingCacheBoard;
	protected final BitBoard bitBoard;
	protected final PositionStateReader positionState;
	protected final FullScanSquareCaptured fullScanSquareCapturer;
	
	public CheckMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard, PositionStateReader positionState) {
		this.dummySquareBoard = dummySquareBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.bitBoard = bitBoard;
		this.positionState = positionState;
		this.fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);
	}
	
	@Override
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummySquareBoard);
		move.executeMove(this.bitBoard);

		if(! fullScanSquareCapturer.isCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.bitBoard);
		move.undoMove(this.dummySquareBoard);
		
		return result;
	}	
	
	@Override
	public boolean filterMoveKing(MoveKing move) {
		move.executeMove(this.kingCacheBoard);

		boolean result = filterMove(move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	@Override
	public boolean filterMoveCastling(MoveCastling moveCastling) {
		return false;
	}		

}
