package layers;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.BitSquareIterator;
import iterators.SquareIterator;

public class ColorBoard {
	
	protected long squareBlancos = 0;
	protected long squareNegros = 0;
	private KingCacheBoard kingCacheBoard = null;
	
	public ColorBoard(PosicionPiezaBoard board) {
		settupSquares(board);
	}
	
	public void swapPositions(Color color, Square remove, Square add){
		if(Color.BLANCO.equals(color)){
			squareBlancos &= ~remove.getPosicion();
			
			squareBlancos |= add.getPosicion();
		} else {
			squareNegros &= ~remove.getPosicion();
			
			squareNegros |= add.getPosicion();
		}
	}
	
	public void addPositions(PosicionPieza position){
		if(Color.BLANCO.equals(position.getValue().getColor())){
			squareBlancos |= position.getKey().getPosicion();
		} else {
			squareNegros |= position.getKey().getPosicion();
		}
	}
	
	public void removePositions(PosicionPieza position){
		if(Color.BLANCO.equals(position.getValue().getColor())){
			squareBlancos &= ~position.getKey().getPosicion();
		} else {
			squareNegros &= ~position.getKey().getPosicion();
		}
	}		
	

	public SquareIterator iteratorSquare(Color color){
		return Color.BLANCO.equals(color) ? new BitSquareIterator(squareBlancos) : new BitSquareIterator(squareNegros);		
	}
	
	public SquareIterator iteratorSquareWhitoutKing(Color color){
		return Color.BLANCO.equals(color) ? new BitSquareIterator(squareBlancos & ~kingCacheBoard.getSquareKingBlancoCache().getPosicion()) : new BitSquareIterator(squareNegros  & ~kingCacheBoard.getSquareKingNegroCache().getPosicion());		
	}
	
	public long getPosiciones (Color color){
		return Color.BLANCO.equals(color) ? squareBlancos : squareNegros;		
	}
	
	public boolean isEmpty(Square destino) {
		return ((~(squareBlancos | squareNegros)) &  destino.getPosicion()) != 0 ;
	}	
	
	public boolean isColor(Color color, Square square) {
		if(Color.BLANCO.equals(color)){
			return (squareBlancos & square.getPosicion()) != 0;
		} else if(Color.NEGRO.equals(color)){
			return (squareNegros & square.getPosicion()) != 0;
		} else{
			throw new RuntimeException("Empty square");
		}
	}

	public Color getColor(Square square) {
		if ((squareBlancos & square.getPosicion()) != 0) {
			return Color.BLANCO;
		} else if ((squareNegros & square.getPosicion()) != 0) {
			return Color.NEGRO;
		}
		return null;
	}
	
	
	//TODO: quitar este metodo de carga
	protected void settupSquares(PosicionPiezaBoard board) {
		for (PosicionPieza posicionPieza : board) {
			Pieza pieza = posicionPieza.getValue();
			if (pieza != null) {
				if (Color.BLANCO.equals(pieza.getColor())) {
					squareBlancos |= posicionPieza.getKey().getPosicion();
				} else if (Color.NEGRO.equals(pieza.getColor())) {
					squareNegros |= posicionPieza.getKey().getPosicion();
				}
			}			
		}
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}	
	
}


