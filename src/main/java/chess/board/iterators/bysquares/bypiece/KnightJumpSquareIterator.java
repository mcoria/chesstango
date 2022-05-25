package chess.board.iterators.bysquares.bypiece;

import chess.board.Square;
import chess.board.iterators.bysquares.JumpSquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class KnightJumpSquareIterator extends JumpSquareIterator {

	public final static int[][] KNIGHT_JUMPS = {
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

	public KnightJumpSquareIterator(Square startingPoint) {
		super(startingPoint, KNIGHT_JUMPS);
	}

}
