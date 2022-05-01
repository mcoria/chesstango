package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;


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
		positionState.setEnPassantSquare(pawnPasanteSquare);
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
        return super.equals(obj) && obj instanceof SaltoDoblePawnMove;
    }
}
