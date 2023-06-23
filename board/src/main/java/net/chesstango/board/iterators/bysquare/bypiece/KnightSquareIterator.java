package net.chesstango.board.iterators.bysquare.bypiece;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.JumpSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class KnightSquareIterator extends JumpSquareIterator {

	public final static int[][] KNIGHT_JUMPS_OFFSETS = {
			//Arriba
			{ -1, 2 },
			{ 1, 2 },

			//Derecha
			{ 2, -1 },
			{ 2, 1 },

			//Izquierda
			{ -2, -1 },
			{ -2, 1 },

			//Abajo
			{ -1, -2 },
			{ 1, -2 },
	};

	public KnightSquareIterator(Square startingPoint) {
		super(startingPoint, KNIGHT_JUMPS_OFFSETS);
	}

}
