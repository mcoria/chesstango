package net.chesstango.board.iterators.byposition.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.BitIterator;
import net.chesstango.board.iterators.byposition.GetElementByIndex;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlackBitIterator<T> extends BitIterator<T> {

	public PawnBlackBitIterator(GetElementByIndex<T> getElementById, Square startingPoint) {
		super(getElementById, startingPoint.getPawnBlackCaptureJumps());
	}
}
