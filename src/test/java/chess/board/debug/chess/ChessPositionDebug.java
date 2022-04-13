package chess.board.debug.chess;

import chess.board.moves.Move;
import chess.board.position.imp.ChessPositionImp;
import chess.board.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionDebug extends ChessPositionImp {
	

	private MoveGeneratorImp moveGeneratorImp;


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
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement, this.moveGeneratorImp);
	}
	
	@Override
	public void init() {
		super.init();
		((PositionStateDebug)positionState).validar(this.piecePlacement);
		((ColorBoardDebug)colorBoard).validar(this.piecePlacement);
		((KingCacheBoardDebug)kingCacheBoard).validar(this.piecePlacement);
		((MoveCacheBoardDebug)moveCache).validar(this.piecePlacement);			
	}


	public void setMoveGeneratorImp(MoveGeneratorImp moveGeneratorImp) {
		this.moveGeneratorImp = moveGeneratorImp;
	}

}