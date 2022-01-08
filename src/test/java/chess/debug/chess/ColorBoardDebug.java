package chess.debug.chess;

import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import chess.iterators.BitSquareIterator;
import chess.iterators.SquareIterator;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;


/**
 * @author Mauricio Coria
 *
 */
public class ColorBoardDebug extends ColorBoard {

	public ColorBoardDebug(PosicionPiezaBoard board) {
		super(board);
	}

	@Override
	public void swapPositions(Color color, Square remove, Square add) {
		super.swapPositions(color, remove, add);
		validar();
	}	
	
	@Override
	public void addPositions(PosicionPieza position) {
		super.addPositions(position);
		validar();
	}
	
	@Override
	public void removePositions(PosicionPieza position) {
		super.removePositions(position);
		validar();
	}	
	
	public void validar() {
		long solapados = this.squareBlancos & this.squareNegros;
		if( solapados != 0 ){
			String solapadosStr = "";
			for (SquareIterator iterator =  new BitSquareIterator(solapados); iterator.hasNext();) {
				solapadosStr = solapadosStr + iterator.next().toString() + " ";
			}

			throw new RuntimeException("El mismo casillero esta tomado por blanca y negro: " + solapadosStr);
		}
	}
	
	public void validar(PosicionPiezaBoard board) {
		validar();
		
		for (PosicionPieza posicionPieza : board) {
			if(posicionPieza.getValue() == null){				
				if(! this.isEmpty(posicionPieza.getKey()) ){
					throw new RuntimeException("ColorBoard contiene una pieza " + this.getColor(posicionPieza.getKey()) + " en " + posicionPieza.getKey() + " pero en PosicionPieza esta vacia");
				}
			} else {
				Color colorBoard = posicionPieza.getValue().getColor();
				Color color = getColor(posicionPieza.getKey());
				
				if(! colorBoard.equals(color) ){
					throw new RuntimeException("PosicionPieza contiene una pieza de color distinto a ColorBoard en " + posicionPieza.getKey());
				}				
				
			}
		}
		

	}
}
