/**
 * 
 */
package chess.debug.chess;

import chess.BoardStatus;
import chess.Game;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.position.imp.ArrayPiecePlacement;

/**
 * @author Mauricio Coria
 *
 */
public class GameDebug extends Game {
	
	protected PiecePlacement piecePlacement = null;
	protected ColorBoard colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;	

	/**
	 * @param tablero
	 */
	public GameDebug(ChessPosition tablero) {
		super(tablero);
	}

	@Override
	// TODO: validadciones del metodo getBoardStatus(): 
	//      								a) no modifica el tablero
	//										b) no modifica el estado
	//										c) no modifica el cache de king
	public BoardStatus getBoardStatus() {
		try {
			boolean reportError = false;
			
			ArrayPiecePlacement boardInicial = ((ArrayPiecePlacement) this.piecePlacement).clone();
			
			KingCacheBoard kingCacheBoardInicial = this.kingCacheBoard.clone();
			
			PositionState boardStateInicial = this.positionState.clone();

			BoardStatus result = super.getBoardStatus();
			
			if (!this.positionState.equals(boardStateInicial)) {
				System.out.println("El estado fué modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + this.positionState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!this.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fué modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + this.kingCacheBoard.toString() + "]\n");
				reportError = true;
			}

			if (!this.piecePlacement.equals(boardInicial)) {
				System.out.println("El board fué modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + this.piecePlacement.toString());
				reportError = true;				
			}

			if (reportError) {
				throw new RuntimeException("Hubo modificaciones ! ! !");
			}
			
			return result;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setDummyBoard(PiecePlacement dummyBoard) {
		this.piecePlacement = dummyBoard;
	}

	public void setColorBoard(ColorBoard colorBoard) {
		this.colorBoard = colorBoard;
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}


	public void setMoveCache(MoveCacheBoard moveCache) {
		this.moveCache = moveCache;
	}


	public PositionState getBoardState() {
		return positionState;
	}


	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}	
}
