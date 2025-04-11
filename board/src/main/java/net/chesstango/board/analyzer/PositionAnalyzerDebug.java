package net.chesstango.board.analyzer;

import lombok.Setter;
import net.chesstango.board.internal.position.KingSquareImp;
import net.chesstango.board.internal.position.PositionStateImp;
import net.chesstango.board.internal.position.SquareBoardImp;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 *
 */
public class PositionAnalyzerDebug extends PositionAnalyzer {
	
	protected SquareBoardReader squareBoardReader = null;
	@Setter
    protected BitBoardReader colorBoard = null;
	@Setter
    protected KingSquareImp kingCacheBoard = null;
	@Setter
    protected MoveCacheBoardWriter moveCache = null;
	protected PositionState positionState = null;

	@Override
	// TODO: validadciones del metodo getBoardStatus(): 
	//      								a) no modifica el tablero
	//										b) no modifica el estado
	//										c) no modifica el cache de king
	public AnalyzerResult analyze() {
		try {
			boolean reportError = false;
			
			SquareBoardReader boardInicial =  ((SquareBoardImp)this.squareBoardReader).clone();
			
			KingSquareImp kingCacheBoardInicial = this.kingCacheBoard.clone();

			PositionStateImp boardStateInicial = ((PositionStateImp) positionState).clone();

			AnalyzerResult result = super.analyze();
			
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

			if (!this.squareBoardReader.equals(boardInicial)) {
				System.out.println("El board fu� modificado");
				System.out.println("Inicial:\n" + boardInicial.toString() + "\n" + "Final:\n" + this.squareBoardReader.toString());
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
	
	public void setPiecePlacement(SquareBoardReader dummyBoard) {
		this.squareBoardReader = dummyBoard;
	}


    public PositionState getBoardState() {
		return positionState;
	}


	public void setBoardState(PositionStateImp positionStateWriter) {
		this.positionState = positionStateWriter;
	}	
}
