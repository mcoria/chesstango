package iterators;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Iterator;

import chess.Pieza;

public class BottomUpIterator implements Iterator<SimpleImmutableEntry<Integer, Pieza>>{

	private Pieza[] tablero;
	private int currentIdx;
	
	public BottomUpIterator(Pieza[] tablero) {
		if(tablero == null || tablero.length != 64 ){
			throw new RuntimeException("Tablero con insuficiontes squares.");
		}
		this.tablero = tablero;
		this.currentIdx = 0;
	}
	
	@Override
	public boolean hasNext() {
		return this.currentIdx < 64;
	}

	@Override
	public SimpleImmutableEntry<Integer, Pieza> next() {
		Integer idx = this.currentIdx;
		Pieza pieza = tablero[this.currentIdx];
		SimpleImmutableEntry<Integer, Pieza> entry = new SimpleImmutableEntry<Integer, Pieza>(idx, pieza);
		this.currentIdx++;
		return entry;
	}

}
