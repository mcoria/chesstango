package net.chesstango.board.moves.generators.legal.strategies.check;

import net.chesstango.board.Color;
import net.chesstango.board.moves.generators.legal.squarecapturers.FullScanSquareCaptured;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.imp.MoveCastlingImp;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.moves.imp.MoveKingImp;
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
public class CheckLegalMoveFilter implements LegalMoveFilter {
	
	protected final SquareBoard dummySquareBoard;
	protected final KingSquareImp kingCacheBoard;
	protected final BitBoard bitBoard;
	protected final PositionStateReader positionState;
	protected final FullScanSquareCaptured fullScanSquareCapturer;
	
	public CheckLegalMoveFilter(SquareBoard dummySquareBoard, KingSquareImp kingCacheBoard, BitBoard bitBoard, PositionStateReader positionState) {
		this.dummySquareBoard = dummySquareBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.bitBoard = bitBoard;
		this.positionState = positionState;
		this.fullScanSquareCapturer = new FullScanSquareCaptured(dummySquareBoard, bitBoard);
	}
	
	@Override
	public boolean isLegalMove(MoveImp move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.doMove(this.dummySquareBoard);
		move.doMove(this.bitBoard);

		if(! fullScanSquareCapturer.isCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.bitBoard);
		move.undoMove(this.dummySquareBoard);
		
		return result;
	}	
	
	@Override
	public boolean isLegalMove(MoveKingImp move) {
		move.doMove(this.kingCacheBoard);

        boolean result = isLegalMove((MoveImp) move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	@Override
	public boolean isLegalMove(MoveCastlingImp moveCastling) {
		return false;
	}		

}
