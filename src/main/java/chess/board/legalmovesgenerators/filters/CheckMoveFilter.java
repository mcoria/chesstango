/**
 * 
 */
package chess.board.legalmovesgenerators.filters;

import chess.board.Color;
import chess.board.analyzer.capturers.Capturer;
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
public class CheckMoveFilter implements MoveFilter {
	
	protected final PiecePlacement dummyBoard;
	protected final KingCacheBoard kingCacheBoard;
	protected final ColorBoard colorBoard;	
	protected final PositionState positionState;

	protected final Capturer capturer;
	
	public CheckMoveFilter(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard, PositionState positionState) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.capturer = new Capturer(dummyBoard);
	}
	
	@Override
	public boolean filterMove(Move move) {
		boolean result = false;
		
		final Color turnoActual = positionState.getTurnoActual();
		final Color opositeTurnoActual = turnoActual.oppositeColor();
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);

		if(! capturer.positionCaptured(opositeTurnoActual, kingCacheBoard.getKingSquare(turnoActual)) ) {
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
