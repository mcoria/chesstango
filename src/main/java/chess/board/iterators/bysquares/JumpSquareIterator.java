package chess.board.iterators.bysquares;

import chess.board.Square;
import chess.board.iterators.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public class JumpSquareIterator implements SquareIterator {
	
	private final int[][] saltos;
	private final Square startingPoint;
	
	private Square nextPoint;
	private int idx = 0;

	public JumpSquareIterator(Square startingPoint, int[][] saltos) {
		this.startingPoint = startingPoint;
		this.saltos = saltos;
		calcularNextPoint();
	}

	@Override
	public boolean hasNext() {
		return this.nextPoint != null;
	}

	@Override
	public Square next() {
		Square currentPoint = this.nextPoint;
		calcularNextPoint();
		return currentPoint;
	}
	
	private void calcularNextPoint() {
		this.nextPoint = null;
		while (this.idx < saltos.length && nextPoint == null) {
			this.nextPoint = Square.getSquare(startingPoint.getFile() + saltos[idx][0],
					startingPoint.getRank() + saltos[idx][1]);
			this.idx++;
		}
	}	

}
