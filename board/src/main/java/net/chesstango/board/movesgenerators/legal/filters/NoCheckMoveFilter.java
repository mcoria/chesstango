/**
 * 
 */
package net.chesstango.board.movesgenerators.legal.filters;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCastling;
import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.movesgenerators.legal.squarecapturers.CardinalSquareCapturer;
import net.chesstango.board.movesgenerators.legal.squarecapturers.FullScanSquareCapturer;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.PositionState;

/**
 * Este filtro se utiliza cuando el jugador actual no se encuentra en jaque
 *
 * @author Mauricio Coria
 *
 */
public class NoCheckMoveFilter implements MoveFilter {
	
	protected final Board dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionState positionState;

	protected final FullScanSquareCapturer fullScanSquareCapturer;
	protected final CardinalSquareCapturer cardinalSquareCapturer;
	
	public NoCheckMoveFilter(Board dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.fullScanSquareCapturer = new FullScanSquareCapturer(dummyBoard);
		this.cardinalSquareCapturer = new CardinalSquareCapturer(dummyBoard);
	}
	
	@Override
	//TODO: deberiamos crear un filtro especifico para EnPassant?
	//      solo movimientos EnPassant terminan siendo filtrados aca,
	//      el resto de los movimiento notking son filtrados por el NoCheckLegalMoveGenerator
	/**
	 *  Este metodo sirve para filtrar movimientos que no son de rey.
	 *  Dado que no se encuentra en jaque, no pregunta por jaque de knight; king o pawn
	 */
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! cardinalSquareCapturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		
		return result;
	}	
	
	@Override
	public boolean filterMoveKing(MoveKing move) {
		boolean result = false;
		final Color turnoActual = positionState.getCurrentTurn();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.kingCacheBoard);

		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! fullScanSquareCapturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	//TODO: este metodo esta consumiendo el 20% del procesamiento,
	// 		deberia crear CAPTURER especifico para validar castling
	@Override
	public boolean filterMoveCastling(MoveCastling moveCastling) {
		Color opositeColor = moveCastling.getFrom().getPiece().getColor().oppositeColor();
		//assert(!capturer.positionCaptured(oppositeColor, moveCastling.getFrom().getKey())); 					    // El king no esta en jaque... lo asumimos
		return !fullScanSquareCapturer.positionCaptured(opositeColor, moveCastling.getRookTo().getSquare()) 		// El king no puede ser capturado en casillero intermedio
			&& !fullScanSquareCapturer.positionCaptured(opositeColor, moveCastling.getTo().getSquare());  			// El king no puede  ser capturado en casillero destino
		
	}		

}
