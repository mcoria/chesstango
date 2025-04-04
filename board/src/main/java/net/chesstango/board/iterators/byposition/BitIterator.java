package net.chesstango.board.iterators.byposition;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public class BitIterator<T> implements Iterator<T> {
	private long positions;

	private final GetElementByIndex<T> getElementById;

	public BitIterator(GetElementByIndex<T> getElementById, long positions) {
		this.positions = positions;
		this.getElementById = getElementById;
	}

	@Override
	public boolean hasNext() {
		return positions != 0;
	}

	@Override
	public T next() {
		T result = null;
		if (positions != 0) {
			long posicionLng = Long.lowestOneBit(positions);
			result  = getElementById.getElement(Long.numberOfTrailingZeros(posicionLng));
			positions &= ~posicionLng;
		}
		return result;
	}


}
