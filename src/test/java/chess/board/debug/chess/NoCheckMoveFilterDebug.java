package chess.board.debug.chess;

import chess.board.legalmovesgenerators.filters.NoCheckMoveFilter;
import chess.board.moves.Move;
import chess.board.moves.MoveKing;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.ArrayPiecePlacement;
import chess.board.position.imp.ColorBoard;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckMoveFilterDebug extends NoCheckMoveFilter {
	
	public NoCheckMoveFilterDebug(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			PositionState positionState) {
		super(dummyBoard, kingCacheBoard, colorBoard, positionState);
	}

	@Override
	public boolean filterMove(Move move) {
		try {
			boolean reportError = false;

			ArrayPiecePlacement boardInicial = ((ArrayPiecePlacement) super.dummyBoard).clone();
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
			
			PositionState boardStateInicial = super.positionState.clone();
	
			boolean result = super.filterMove(move);
			
			if (!super.positionState.equals(boardStateInicial)) {
				System.out.println("El estado fu� modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + super.positionState + "]\n");
				reportError = true;				
			}			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fu� modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard + "]\n");
				reportError = true;
			}
	
			if (!super.dummyBoard.equals(boardInicial)) {
				System.out.println("El board fu� modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + super.dummyBoard);
				reportError = true;				
			}
	
			if (reportError) {
				System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			((PositionStateDebug)positionState).validar(this.dummyBoard);
			((ColorBoardDebug)colorBoard).validar(this.dummyBoard);	
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean filterMove(MoveKing move) {
		try {
			boolean reportError = false;	
			
			KingCacheBoard kingCacheBoardInicial = super.kingCacheBoard.clone();
	
			boolean result = super.filterMove(move);			
			
			if (!super.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fu� modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + super.kingCacheBoard + "]\n");
				reportError = true;
			}
	
			if (reportError) {
				System.out.println("El filtrado del moviemiento [" + move + "] causo la inconsistencia");
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			((KingCacheBoardDebug)kingCacheBoard).validar(this.dummyBoard);			
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
