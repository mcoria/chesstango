package net.chesstango.board.iterators.byposition.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.BitIterator;
import net.chesstango.board.iterators.byposition.GetElementByIndex;

/**
 * @author Mauricio Coria
 *
 */
public class KingBitIterator<T> extends BitIterator<T> {

	public KingBitIterator(GetElementByIndex<T> getElementById, Square startingPoint) {
		super(getElementById, startingPoint.getKingJumps());
	}

}
