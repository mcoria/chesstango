package net.chesstango.board.iterators.byposition.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.BitIterator;
import net.chesstango.board.position.GetElementByIndex;

/**
 * @author Mauricio Coria
 *
 */
public class PawnWhiteBitIterator<T> extends BitIterator<T> {

	public PawnWhiteBitIterator(GetElementByIndex<T> getElementById, Square startingPoint) {
		super(getElementById, startingPoint.getPawnWhiteCaptureJumps());
	}

}
