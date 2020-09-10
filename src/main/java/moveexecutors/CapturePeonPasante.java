package moveexecutors;

import chess.MoveCache;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.DummyBoard;

public class CapturePeonPasante extends SimpleMove {

	private final PosicionPieza captura;
			
	public CapturePeonPasante(PosicionPieza from, PosicionPieza to, PosicionPieza captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		super.executeMove(board);
		board.setEmptyPosicion(captura);		//Capturamos peon
	}

	@Override
	public void undoMove(DummyBoard board) {
		super.undoMove(board);
		board.setPosicion(captura);			//Devolvemos peon
	}
	
	@Override
	public void executeMove(ColorBoard boardCache) {
		super.executeMove(boardCache);
		boardCache.removePositions(captura);
	}

	@Override
	public void undoMove(ColorBoard boardCache) {
		super.undoMove(boardCache);
		boardCache.addPositions(captura);
	}
	
	@Override
	public void updateMoveChache(MoveCache moveCache) {
		super.updateMoveChache(moveCache);
		moveCache.clearPseudoMovesAffectedBy(captura.getKey());
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
