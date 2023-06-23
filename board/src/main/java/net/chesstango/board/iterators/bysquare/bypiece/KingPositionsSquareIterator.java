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
public class KingPositionsSquareIterator extends PositionsSquareIterator {

	public KingPositionsSquareIterator(Square startingPoint) {
		super(startingPoint.getKingJumps());
	}

}
