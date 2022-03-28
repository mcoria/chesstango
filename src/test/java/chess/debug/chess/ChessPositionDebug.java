package chess.debug.chess;

import chess.moves.Move;
import chess.position.imp.ChessPositionImp;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionDebug extends ChessPositionImp {
	

	@Override
	public void acceptForExecute(Move move) {
		super.acceptForExecute(move);
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);
	}

	@Override
	public void acceptForUndo(Move move) {
		super.acceptForUndo(move);
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);		
	}
	
	@Override
	public void init() {
		super.init();
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);			
	}

}