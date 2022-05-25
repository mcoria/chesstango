package chess.board.iterators.bit;

import chess.board.position.GetElementByIndex;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public class BitIterator<T> implements Iterator<T> {
	private long posiciones;

	private final GetElementByIndex<T> getElementById;

	public BitIterator(GetElementByIndex<T> getElementById, long posiciones) {
		this.posiciones = posiciones;
		this.getElementById = getElementById;
	}

	@Override
	public boolean hasNext() {
		return posiciones != 0;
	}

	@Override
	public T next() {
		T result = null;
		if (posiciones != 0) {
			long posicionLng = Long.lowestOneBit(posiciones);
			result  = getElementById.getElement(Long.numberOfTrailingZeros(posicionLng));
			posiciones &= ~posicionLng;
		}
		return result;
	}


}
