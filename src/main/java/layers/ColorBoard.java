package layers;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.BitSquareIterator;
import iterators.SquareIterator;

public class ColorBoard {
	
	private Square squareKingBlancoCache = null;
	
	private Square squareKingNegroCache = null;
	
	public ColorBoard(DummyBoard board) {
		settupSquares(board);
		
		this.squareKingBlancoCache = getKingSquareRecorrer(Color.BLANCO, board);
		this.squareKingNegroCache = getKingSquareRecorrer(Color.NEGRO, board);		
	}
	
	///////////////////////////// START getKingSquare Logic /////////////////////////////
	
	public void setKingSquare(Color color, Square square) {
		if(Color.BLANCO.equals(color)){
			this.squareKingBlancoCache = square;
		} else {
			this.squareKingNegroCache = square;
		}
	}
	
	public Square getSquareKingBlancoCache() {
		return squareKingBlancoCache;
	}
	
	public Square getSquareKingNegroCache() {
		return squareKingNegroCache;
	}
	
	private Square getKingSquareRecorrer(Color color, DummyBoard board) {
		Square kingSquare = null;
		Pieza rey = Pieza.getRey(color);
		for (PosicionPieza entry : board) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(rey.equals(currentPieza)){
				kingSquare = currentSquare;
				break;
			}
		}
		return kingSquare;
	}
	///////////////////////////// END getKingSquare Logic /////////////////////////////		
	
	///////////////////////////// START Cache Iteration Logic /////////////////////////////	
	// Prestar atencion que este cache se actualiza una vez que realmente se mueven las fichas
	//private ArrayList<Square> squareBlancos = new ArrayList<Square>();
	//private ArrayList<Square> squareNegros = new ArrayList<Square>();
	private long squareBlancos = 0;
	private long squareNegros = 0;
	
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
	
	public long getPosiciones (Color color){
		return Color.BLANCO.equals(color) ? squareBlancos : squareNegros;		
	}
	
	
	protected void settupSquares(DummyBoard board) {
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
	
	///////////////////////////// END Cache Iteration Logic /////////////////////////////		

	/*
	public void validarCacheSqueare(DummyBoard board) {
		int posicionesBlancas = 0;
		int posicionesNegras = 0;
		for (PosicionPieza posicionPieza : board) {
			if(posicionPieza.getValue() != null){
				Color color = posicionPieza.getValue().getColor();
				if(Color.BLANCO.equals(color)){
					posicionesBlancas++;
					if( (squareBlancos & posicionPieza.getKey().getPosicion()) == 0   ){
						throw new RuntimeException("La posicion squareBlancos no se encuentra");
					}
				} else {
					posicionesNegras++;
					if( (squareNegros & posicionPieza.getKey().getPosicion()) == 0){
						throw new RuntimeException("La posicion squareNegros no se encuentra");
					}					
				}
			}
		}
		
		if( Long.bitCount(squareBlancos) != posicionesBlancas ){
			throw new RuntimeException("Diferencias en cantidad de posicions blancas");
		}
		
		if( Long.bitCount(squareNegros) != posicionesNegras ){
			throw new RuntimeException("Diferencias en cantidad de posicions negras");
		}
	}
	*/
}


