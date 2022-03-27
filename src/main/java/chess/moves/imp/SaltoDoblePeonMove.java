package chess.moves.imp;

import chess.PiecePositioned;
import chess.Square;
import chess.position.imp.MoveCacheBoard;
import chess.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
class SaltoDoblePawnMove extends SimpleMove {
	
	private final Square pawnPasanteSquare;

	public SaltoDoblePawnMove(PiecePositioned from, PiecePositioned to, Square pawnPasanteSquare) {
		super(from, to);
		this.pawnPasanteSquare = pawnPasanteSquare;
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setPawnPasanteSquare(pawnPasanteSquare);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();		
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), pawnPasanteSquare, true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), pawnPasanteSquare, false);
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
