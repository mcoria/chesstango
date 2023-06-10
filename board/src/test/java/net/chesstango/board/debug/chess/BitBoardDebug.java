package net.chesstango.board.debug.chess;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.iterators.bysquare.PositionsSquareIterator;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.imp.BitBoardImp;


/**
 * @author Mauricio Coria
 *
 */
public class BitBoardDebug extends BitBoardImp {

	@Override
	public void swapPositions(Color color, Square remove, Square add) {
		super.swapPositions(color, remove, add);
		validar();
	}	
	
	@Override
	public void addPosition(PiecePositioned position) {
		super.addPosition(position);
		validar();
	}
	
	@Override
	public void removePosition(PiecePositioned position) {
		super.removePosition(position);
		validar();
	}	
	
	public void validar() {
		long solapados = this.squareWhites & this.squareBlacks;
		if( solapados != 0 ){
			String solapadosStr = "";
			for (SquareIterator iterator = new PositionsSquareIterator(solapados); iterator.hasNext();) {
				solapadosStr = solapadosStr + iterator.next().toString() + " ";
			}

			throw new RuntimeException("El mismo casillero esta tomado por blanca y negro: " + solapadosStr);
		}
	}
	
	public void validar(SquareBoard squareBoard) {
		validar();
		
		for (PiecePositioned piecePositioned : squareBoard) {
			if(piecePositioned.getPiece() == null){
				if(! this.isEmpty(piecePositioned.getSquare()) ){
					throw new RuntimeException("ColorBoard contiene una pieza " + this.getColor(piecePositioned.getSquare()) + " en " + piecePositioned.getSquare() + " pero en PosicionPieza esta vacia");
				}
			} else {
				Color colorBoard = piecePositioned.getPiece().getColor();
				Color color = getColor(piecePositioned.getSquare());
				
				if(! colorBoard.equals(color) ){
					throw new RuntimeException("PosicionPieza contiene una pieza de color distinto a ColorBoard en " + piecePositioned.getSquare());
				}				
				
			}
		}
		

	}
}
