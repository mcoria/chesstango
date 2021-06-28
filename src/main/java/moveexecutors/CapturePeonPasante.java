package moveexecutors;

import chess.PosicionPieza;
import layers.ColorBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;

public class CapturePeonPasante extends AbstractMove {

	private final PosicionPieza captura;
			
	public CapturePeonPasante(PosicionPieza from, PosicionPieza to, PosicionPieza captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		board.move(from, to);
		board.setEmptyPosicion(captura);		//Capturamos peon
	}

	@Override
	public void undoMove(PosicionPiezaBoard board) {
		board.setPosicion(to);				//Reestablecemos destino
		board.setPosicion(from);			//Volvemos a origen
		board.setPosicion(captura);			//Devolvemos peon
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
		colorBoard.removePositions(captura);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
		colorBoard.addPositions(captura);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushState();
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), captura.getKey());
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
	protected String getType() {
		return "CapturePeonPasante";
	}

}
