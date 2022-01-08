package chess.moves;

import chess.BoardState;
import chess.PosicionPieza;
import chess.Square;
import chess.layers.MoveCacheBoard;


/**
 * @author Mauricio Coria
 *
 */
class SaltoDoblePawnMove extends SimpleMove {
	
	private final Square peonPasanteSquare;

	public SaltoDoblePawnMove(PosicionPieza from, PosicionPieza to, Square peonPasanteSquare) {
		super(from, to);
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setPawnPasanteSquare(peonPasanteSquare);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();		
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), peonPasanteSquare, true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), peonPasanteSquare, false);
		moveCache.popCleared();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SaltoDoblePawnMove){
			return true;
		}
		return false;
	}
}
