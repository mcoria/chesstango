/**
 * 
 */
package chess.board.legalmovesgenerators.filters;

import chess.board.Color;
import chess.board.analyzer.capturers.Capturer;
import chess.board.analyzer.capturers.NoCheckCapturer;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveCastling;
import chess.board.moves.MoveKing;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class NoCheckMoveFilter implements MoveFilter{
	
	protected final PiecePlacement dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionState positionState;

	protected final Capturer capturer;
	protected final NoCheckCapturer noCheckCapturer;
	
	public NoCheckMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.capturer = new Capturer(dummyBoard);
		this.noCheckCapturer = new NoCheckCapturer(dummyBoard);
	}
	
	@Override
	//TODO: deberiamos crear un filtro especifico para EnPassant? solo movimientos EnPassant terminan siendo filtrados aca, el resto de los movimiento notking son filtrados por el NoCheckLegalMoveGenerator, 
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getTurnoActual();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! noCheckCapturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
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

		if(! capturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	//TODO: este metodo esta consumiendo el 20% del procesamiento, deberia crear CAPTURER especifico para validar castling
	@Override
	public boolean filterMove(MoveCastling moveCastling) {
		Color opositeColor = moveCastling.getFrom().getValue().getColor().oppositeColor();
		//assert(!capturer.positionCaptured(oppositeColor, moveCastling.getFrom().getKey())); 				// El king no esta en jaque... lo asumimos
		return !capturer.positionCaptured(opositeColor, moveCastling.getRookMove().getTo().getKey()) 	// El king no puede ser capturado en casillero intermedio
			&& !capturer.positionCaptured(opositeColor, moveCastling.getTo().getKey());  				// El king no puede  ser capturado en casillero destino
		
	}		

}
