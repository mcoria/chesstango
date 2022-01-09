package chess.moves;

import chess.ChessPosition;
import chess.PiecePositioned;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;
import chess.pseudomovesfilters.MoveFilter;

//TODO: lo podemos modelar como dos movimientos, similar al enroque. El 1er move una captura; luego un move simple

/**
 * @author Mauricio Coria
 *
 */
class CapturePawnPasante extends AbstractMove {

	private final PiecePositioned captura;
			
	public CapturePawnPasante(PiecePositioned from, PiecePositioned to, PiecePositioned captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}	
	
	@Override
	public void executeMove(PiecePlacement board) {
		super.executeMove(board);
		board.setEmptyPosicion(captura);		//Capturamos peon
	}

	@Override
	public void undoMove(PiecePlacement board) {
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
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), captura.getKey(), true);		
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), captura.getKey(), false);
		moveCache.popCleared();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj) && obj instanceof CapturePawnPasante){
			CapturePawnPasante theOther = (CapturePawnPasante) obj;
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
