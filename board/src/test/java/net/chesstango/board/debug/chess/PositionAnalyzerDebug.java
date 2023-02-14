/**
 * 
 */
package net.chesstango.board.debug.chess;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.imp.ArrayPiecePlacement;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.KingCacheBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class PositionAnalyzerDebug extends PositionAnalyzer {
	
	protected PiecePlacementReader piecePlacementReader = null;
	protected ColorBoard colorBoard = null;
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
			
			PiecePlacementReader boardInicial =  ((ArrayPiecePlacement)this.piecePlacementReader).clone();
			
			KingCacheBoard kingCacheBoardInicial = this.kingCacheBoard.clone();
			
			PositionState boardStateInicial = this.positionState.clone();

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

			if (!this.piecePlacementReader.equals(boardInicial)) {
				System.out.println("El board fu� modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + this.piecePlacementReader.toString());
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
	
	public void setPiecePlacement(PiecePlacementReader dummyBoard) {
		this.piecePlacementReader = dummyBoard;
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