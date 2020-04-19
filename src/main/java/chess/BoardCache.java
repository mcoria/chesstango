package chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	public List<Square> squareBlancos = new ArrayList<Square>();
	public List<Square> squareNegros = new ArrayList<Square>();
	
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
}


