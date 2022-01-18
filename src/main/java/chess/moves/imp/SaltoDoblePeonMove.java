package chess.moves.imp;

import chess.PiecePositioned;
import chess.Square;
import chess.position.MoveCacheBoard;
import chess.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
class SaltoDoblePawnMove extends SimpleMove {
	
	private final Square peonPasanteSquare;

	public SaltoDoblePawnMove(PiecePositioned from, PiecePositioned to, Square peonPasanteSquare) {
		super(from, to);
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setPawnPasanteSquare(peonPasanteSquare);
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
