package chess.board.legalmovesgenerators;

import chess.board.Color;
import chess.board.analyzer.Capturer;
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
