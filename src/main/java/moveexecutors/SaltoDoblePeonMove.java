package moveexecutors;

import chess.BoardState;
import chess.PosicionPieza;
import chess.Square;
import layers.MoveCacheBoard;

public class SaltoDoblePeonMove extends SimpleMove {
	
	private final Square peonPasanteSquare;

	public SaltoDoblePeonMove(PosicionPieza from, PosicionPieza to, Square peonPasanteSquare) {
		super(from, to);
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setPeonPasanteSquare(peonPasanteSquare);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), peonPasanteSquare);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey());
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SaltoDoblePeonMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SaltoDoblePeonMove";
	}
}
