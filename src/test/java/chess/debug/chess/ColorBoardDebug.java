package chess.debug.chess;

import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.square.BitSquareIterator;
import chess.iterators.square.SquareIterator;
import chess.position.ColorBoard;
import chess.position.PiecePlacement;


/**
 * @author Mauricio Coria
 *
 */
public class ColorBoardDebug extends ColorBoard {

	@Override
	public void swapPositions(Color color, Square remove, Square add) {
		super.swapPositions(color, remove, add);
		validar();
	}	
	
	@Override
	public void addPositions(PiecePositioned position) {
		super.addPositions(position);
		validar();
	}
	
	@Override
	public void removePositions(PiecePositioned position) {
		super.removePositions(position);
		validar();
	}	
	
	public void validar() {
		long solapados = this.squareWhites & this.squareBlacks;
		if( solapados != 0 ){
			String solapadosStr = "";
			for (SquareIterator iterator =  new BitSquareIterator(solapados); iterator.hasNext();) {
				solapadosStr = solapadosStr + iterator.next().toString() + " ";
			}

			throw new RuntimeException("El mismo casillero esta tomado por blanca y negro: " + solapadosStr);
		}
	}
	
	public void validar(PiecePlacement board) {
		validar();
		
		for (PiecePositioned piecePositioned : board) {
			if(piecePositioned.getValue() == null){				
				if(! this.isEmpty(piecePositioned.getKey()) ){
					throw new RuntimeException("ColorBoard contiene una pieza " + this.getColor(piecePositioned.getKey()) + " en " + piecePositioned.getKey() + " pero en PosicionPieza esta vacia");
				}
			} else {
				Color colorBoard = piecePositioned.getValue().getColor();
				Color color = getColor(piecePositioned.getKey());
				
				if(! colorBoard.equals(color) ){
					throw new RuntimeException("PosicionPieza contiene una pieza de color distinto a ColorBoard en " + piecePositioned.getKey());
				}				
				
			}
		}
		

	}
}
