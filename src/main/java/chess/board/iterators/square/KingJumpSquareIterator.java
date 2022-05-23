package chess.board.iterators.square;

import chess.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public class KingJumpSquareIterator extends JumpSquareIterator {

	public final static int[][] SALTOS_KING = {
			{ 0, 1 }, // Norte
			{ 1, 1 },   // NE
			{ -1, 1 },  // NO
			{ 0, -1 },  // Sur
			{ 1, -1 },  // SE
			{ -1, -1 }, // SO
			{ 1, 0 },   // Este
			{ -1, 0 },  // Oeste
	};

	public KingJumpSquareIterator(Square startingPoint) {
		super(startingPoint, SALTOS_KING);
	}

}
