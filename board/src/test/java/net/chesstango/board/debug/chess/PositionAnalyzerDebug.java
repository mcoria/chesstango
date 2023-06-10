/**
 * 
 */
package net.chesstango.board.debug.chess;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.ColorBoardReader;
import net.chesstango.board.position.PositionState;
import net.chesstango.board.position.imp.*;

/**
 * @author Mauricio Coria
 *
 */
public class PositionAnalyzerDebug extends PositionAnalyzer {
	
	protected BoardReader boardReader = null;
	protected ColorBoardReader colorBoard = null;
	protected KingCacheBoard kingCacheBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;

	@Override
	// TODO: validadciones del metodo getBoardStatus(): 
	//      								a) no modifica el tablero
	//										b) no modifica el estado
	//										c) no modifica el cache de king
	public AnalyzerResult analyze() {
		try {
			boolean reportError = false;
			
			BoardReader boardInicial =  ((ArrayBoard)this.boardReader).clone();
			
			KingCacheBoard kingCacheBoardInicial = this.kingCacheBoard.clone();

			PositionStateImp boardStateInicial = ((PositionStateImp)positionState).clone();

			AnalyzerResult result = analyze();
			
			if (!this.positionState.equals(boardStateInicial)) {
				System.out.println("El estado fue modificado");
				System.out.println("Inicial [" + boardStateInicial.toString() + "]\n" + "Final   [" + this.positionState.toString() + "]\n");
				reportError = true;				
			}			
			
			if (!this.kingCacheBoard.equals(kingCacheBoardInicial)) {
				System.out.println("El cache de king fu� modificado");
				System.out.println("Inicial [" + kingCacheBoardInicial.toString() + "]\n" + "Final   [" + this.kingCacheBoard.toString() + "]\n");
				reportError = true;
			}

			if (!this.boardReader.equals(boardInicial)) {
				System.out.println("El board fu� modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + this.boardReader.toString());
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
	
	public void setPiecePlacement(BoardReader dummyBoard) {
		this.boardReader = dummyBoard;
	}

	public void setColorBoard(ColorBoardReader colorBoard) {
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


	public void setBoardState(PositionStateImp positionStateWriter) {
		this.positionState = positionStateWriter;
	}	
}
