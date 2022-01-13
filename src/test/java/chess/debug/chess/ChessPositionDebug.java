package chess.debug.chess;

import chess.moves.Move;
import chess.position.ChessPosition;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionDebug extends ChessPosition {
	

	@Override
	public void execute(Move move) {
		super.execute(move);
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);
	}

	@Override
	public void undo(Move move) {
		super.undo(move);
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);		
	}

}
