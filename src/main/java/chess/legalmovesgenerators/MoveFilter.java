package chess.legalmovesgenerators;

import chess.Color;
import chess.analyzer.Capturer;
import chess.moves.Move;
import chess.moves.MoveCastling;
import chess.moves.MoveKing;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;


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
	
	public boolean filterMove(MoveKing move) {
		boolean result = false;
		
		move.executeMove(this.kingCacheBoard);

		result = filterMove((Move)move);

		move.undoMove(this.kingCacheBoard);
		
		return result;
	}

	public boolean filterMove(MoveCastling moveCastling) {
		Color opositeColor = moveCastling.getFrom().getValue().getColor().opositeColor();
		return !capturer.positionCaptured(opositeColor, moveCastling.getFrom().getKey()) 				// El king no esta en jaque
			&& !capturer.positionCaptured(opositeColor, moveCastling.getRookMove().getTo().getKey()) 	// El king no puede ser capturado en casillero intermedio
			&& !capturer.positionCaptured(opositeColor, moveCastling.getTo().getKey());  				// El king no puede  ser capturado en casillero destino
		
	}	

}
