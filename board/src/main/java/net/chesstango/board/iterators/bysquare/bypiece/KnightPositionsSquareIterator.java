/**
 * 
 */
package net.chesstango.board.iterators.bysquare.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.PositionsSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class KnightPositionsSquareIterator extends PositionsSquareIterator {

	public KnightPositionsSquareIterator(Square startingPoint) {
		super(startingPoint.getKnightJumps());
	}

}
