package iterators;

import java.util.Iterator;
import java.util.AbstractMap.SimpleImmutableEntry;

import chess.Pieza;

public class TopDownIterator implements Iterator<SimpleImmutableEntry<Integer, Pieza>> {

	private Pieza[] tablero;
	private int rank = 7;
	private int file = 0;
	
	public TopDownIterator(Pieza[] tablero) {
		if(tablero == null || tablero.length != 64 ){
			throw new RuntimeException("Tablero con insuficiontes squares.");
		}
		this.tablero = tablero;
	}
	
	@Override
	public boolean hasNext() {
		return this.rank >= 0;
	}

	@Override
	public SimpleImmutableEntry<Integer, Pieza> next() {
		Integer idx = this.rank * 8 + file;
		Pieza pieza = tablero[idx];
		SimpleImmutableEntry<Integer, Pieza> entry = new SimpleImmutableEntry<Integer, Pieza>(idx, pieza);
		if(file < 8){
			file++;
		}
		if(file == 8){
			file = 0;
			rank--;
		}
		return entry;
	}

}
