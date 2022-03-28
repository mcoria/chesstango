package chess.moves.imp;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPositionWriter;
import chess.position.PiecePlacementWritter;
import chess.position.imp.ColorBoard;
import chess.position.imp.MoveCacheBoard;

//TODO: lo podemos modelar como dos movimientos, similar al castling. El 1er move una captura; luego un move simple

/**
 * @author Mauricio Coria
 *
 */
class CaptureEnPassant extends AbstractMove {

	private final PiecePositioned captura;
			
	public CaptureEnPassant(PiecePositioned from, PiecePositioned to, PiecePositioned captura) {
		super(from, to);
		this.captura = captura;
	}
	
	@Override
	public void executeMove(ChessPositionWriter chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPositionWriter chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}	
	
	@Override
	public void executeMove(PiecePlacementWritter board) {
		super.executeMove(board);
		board.setEmptyPosicion(captura);		//Capturamos pawn
	}

	@Override
	public void undoMove(PiecePlacementWritter board) {
		super.undoMove(board);
		board.setPosicion(captura);				//Devolvemos pawn
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
		if(super.equals(obj) && obj instanceof CaptureEnPassant){
			CaptureEnPassant theOther = (CaptureEnPassant) obj;
			return captura.equals(theOther.captura) ;
		}
		return false;
	}

}
