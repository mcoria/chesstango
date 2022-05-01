/**
 * 
 */
package chess.board.legalmovesgenerators.filters;

import chess.board.Color;
import chess.board.legalmovesgenerators.squarecapturers.FullScanSquareCapturer;
import chess.board.legalmovesgenerators.squarecapturers.CardinalSquareCapturer;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveKing;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;

/**
 * Este filtro se utiliza cuando el jugador actual no se encuentra en jaque
 *
 * @author Mauricio Coria
 *
 */
public class NoCheckMoveFilter implements MoveFilter {
	
	protected final PiecePlacement dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionState positionState;

	protected final FullScanSquareCapturer fullScanSquareCapturer;
	protected final CardinalSquareCapturer cardinalSquareCapturer;
	
	public NoCheckMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState) {
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
		
		final Color turnoActual = positionState.getTurnoActual();
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
	public boolean filterMove(MoveKing move) {
		boolean result = false;
		final Color turnoActual = positionState.getTurnoActual();
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
	public boolean filterMove(MoveCastling moveCastling) {
		Color opositeColor = moveCastling.getFrom().getValue().getColor().oppositeColor();
		//assert(!capturer.positionCaptured(oppositeColor, moveCastling.getFrom().getKey())); 							// El king no esta en jaque... lo asumimos
		return !fullScanSquareCapturer.positionCaptured(opositeColor, moveCastling.getRookMove().getTo().getKey()) 		// El king no puede ser capturado en casillero intermedio
			&& !fullScanSquareCapturer.positionCaptured(opositeColor, moveCastling.getTo().getKey());  					// El king no puede  ser capturado en casillero destino
		
	}		

}
