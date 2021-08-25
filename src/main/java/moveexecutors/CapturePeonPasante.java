package moveexecutors;

import chess.Board;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.MoveFilter;

//TODO: lo podemos modelar como dos movimientos, similar al enroque. El 1er move una captura; luego un move simple
public class CapturePeonPasante extends AbstractMove {

	private final PosicionPieza captura;
			
	protected CapturePeonPasante(PosicionPieza from, PosicionPieza to, PosicionPieza captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(Board board) {
		board.executeMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}	
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		super.executeMove(board);
		board.setEmptyPosicion(captura);		//Capturamos peon
	}

	@Override
	public void undoMove(PosicionPiezaBoard board) {
		super.undoMove(board);
		board.setPosicion(captura);				//Devolvemos peon
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.removePositions(captura);
		
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
		
		colorBoard.addPositions(captura);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), captura.getKey());
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(to.getKey(), from.getKey(), captura.getKey());
	}		
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj) && obj instanceof CapturePeonPasante){
			CapturePeonPasante theOther = (CapturePeonPasante) obj;
			return captura.equals(theOther.captura) ;
		}
		return false;
	}

	@Override
	public void executeMove(KingCacheBoard kingCacheBoard) {
		throw new RuntimeException("Error !");
	}

	@Override
	public void undoMove(KingCacheBoard kingCacheBoard) {
		throw new RuntimeException("Error !");
	}

}
