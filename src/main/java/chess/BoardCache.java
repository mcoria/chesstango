package chess;

import java.util.ArrayList;
import java.util.Iterator;

import iterators.SquareIterator;

public class BoardCache {
	
	private Square squareKingBlancoCache = null;
	
	private Square squareKingNegroCache = null;
	
	public BoardCache(Board board) {
		settupSquares(board);
		
		setSquareKingBlancoCache(getKingSquareRecorrer(Color.BLANCO, board));
		setSquareKingNegroCache(getKingSquareRecorrer(Color.NEGRO, board));		
	}
	
	///////////////////////////// START getKingSquare Logic /////////////////////////////
	public Square getKingSquare(Color color) {
		return Color.BLANCO.equals(color) ? getSquareKingBlancoCache() : getSquareKingNegroCache();
	}
	
	public Square getSquareKingBlancoCache() {
		return squareKingBlancoCache;
	}
	
	public void setSquareKingBlancoCache(Square squareKingBlancoCache) {
		this.squareKingBlancoCache = squareKingBlancoCache;
	}
	
	public Square getSquareKingNegroCache() {
		return squareKingNegroCache;
	}
	
	public void setSquareKingNegroCache(Square squareKingNegroCache) {
		this.squareKingNegroCache = squareKingNegroCache;
	}
	
	private Square getKingSquareRecorrer(Color color, Board board) {
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
	public ArrayList<Square> squareBlancos = new ArrayList<Square>();
	public ArrayList<Square> squareNegros = new ArrayList<Square>();
	
	public SquareIterator iteratorSquare(Color color){
		return new SquareIterator(){
			private Iterator<Square> iterator = Color.BLANCO.equals(color) ? squareBlancos.iterator() : squareNegros.iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			
			@Override
			public Square next() {
				return iterator.next();
			}
		};		
	}
	
	public void settupSquares(Board board) {
		for (PosicionPieza posicionPieza : board) {
			Pieza pieza = posicionPieza.getValue();
			if (pieza != null) {
				if (Color.BLANCO.equals(pieza.getColor())) {
					squareBlancos.add(posicionPieza.getKey());
				} else if (Color.NEGRO.equals(pieza.getColor())) {
					squareNegros.add(posicionPieza.getKey());
				}
			}			
		}
	}	
	///////////////////////////// START Cache Iteration Logic /////////////////////////////		

	public void validarCacheSqueare(DummyBoard board) {
		int posicionesBlancas = 0;
		int posicionesNegras = 0;
		for (PosicionPieza posicionPieza : board) {
			if(posicionPieza.getValue() != null){
				Color color = posicionPieza.getValue().getColor();
				if(Color.BLANCO.equals(color)){
					posicionesBlancas++;
					if(! squareBlancos.contains(posicionPieza.getKey() )){
						throw new RuntimeException("La posicion squareBlancos no se encuentra");
					}
				} else {
					posicionesNegras++;
					if(! squareNegros.contains(posicionPieza.getKey() )){
						throw new RuntimeException("La posicion squareNegros no se encuentra");
					}					
				}
			}
		}
		
		if( posicionesBlancas != squareBlancos.size() ){
			throw new RuntimeException("Diferencias en cantidad de posicions blancas");
		}
		
		if( posicionesNegras != squareNegros.size() ){
			throw new RuntimeException("Diferencias en cantidad de posicions negras");
		}		
		
	}
}


