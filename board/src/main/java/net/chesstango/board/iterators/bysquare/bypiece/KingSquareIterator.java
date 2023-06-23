package net.chesstango.board.iterators.bysquare.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.JumpSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class KingSquareIterator extends JumpSquareIterator {

	public final static int[][] KING_JUMP_OFFSETS = {
			{ 0, 1 }, // Norte
			{ 1, 1 },   // NE
			{ -1, 1 },  // NO
			{ 0, -1 },  // Sur
			{ 1, -1 },  // SE
			{ -1, -1 }, // SO
			{ 1, 0 },   // Este
			{ -1, 0 },  // Oeste
	};

	public KingSquareIterator(Square startingPoint) {
		super(startingPoint, KING_JUMP_OFFSETS);
	}

}
