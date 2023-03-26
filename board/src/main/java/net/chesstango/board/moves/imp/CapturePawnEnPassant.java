package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;

//TODO: lo podemos modelar como dos movimientos, similar al castling. El 1er move una captura; luego un move simple

/**
 * @author Mauricio Coria
 *
 */
class CapturePawnEnPassant extends AbstractPawnMove {

	private final PiecePositioned capture;
			
	public CapturePawnEnPassant(PiecePositioned from, PiecePositioned to, PiecePositioned capture) {
		super(from, to);
		this.capture = capture;
	}
	
	@Override
	public void executeMove(PiecePlacementWriter board) {
		super.executeMove(board);
		board.setEmptyPosition(capture);		//Capturamos pawn
	}

	@Override
	public void undoMove(PiecePlacementWriter board) {
		super.undoMove(board);
		board.setPosition(capture);				//Devolvemos pawn
	}

	@Override
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.removePositions(capture);
		
		colorBoard.swapPositions(from.getPiece().getColor(), from.getSquare(), to.getSquare());
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getPiece().getColor(), to.getSquare(), from.getSquare());
		
		colorBoard.addPositions(capture);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), capture.getSquare(), true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), capture.getSquare(), false);
		moveCache.popCleared();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj) && obj instanceof CapturePawnEnPassant){
			CapturePawnEnPassant theOther = (CapturePawnEnPassant) obj;
			return capture.equals(theOther.capture) ;
		}
		return false;
	}

}
