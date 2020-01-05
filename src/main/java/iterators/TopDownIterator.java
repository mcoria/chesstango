package iterators;

import java.util.Iterator;
import java.util.AbstractMap.SimpleImmutableEntry;

import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class TopDownIterator implements Iterator<SimpleImmutableEntry<Square, Pieza>> {

	private DummyBoard tablero;
	private Square nextSquare = Square.a8;
	
	public TopDownIterator(DummyBoard tablero) {
		if(tablero == null){
			throw new RuntimeException("Tablero con insuficiontes squares.");
		}
		this.tablero = tablero;
	}
	
	@Override
	public boolean hasNext() {
		return this.nextSquare != null;
	}

	@Override
	public SimpleImmutableEntry<Square, Pieza> next() {
		Pieza pieza = tablero.getPieza(this.nextSquare);
		SimpleImmutableEntry<Square, Pieza> entry = new SimpleImmutableEntry<Square, Pieza>(this.nextSquare, pieza);
		
		int file = this.nextSquare.getFile();
		int rank = this.nextSquare.getRank();
				
		if(file < 8){
			file++;
		}
		
		if(file == 8){
			file = 0;
			rank--;
		}
		
		if(rank >=0 ){
			this.nextSquare = Square.getSquare(file, rank);
		} else {
			this.nextSquare = null;
		}
		
		return entry;
	}
}
