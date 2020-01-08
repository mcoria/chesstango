package iterators;

import chess.Square;

public class ReySquareIterator implements SquareIterator {

	private final int[][] vector = { { 0, 1 }, // Norte
			{ 1, 1 }, // NE
			{ -1, 1 }, // NO
			{ 0, -1 }, // Sur
			{ 1, -1 }, // SE
			{ -1, -1 }, // SO
			{ 1, 0 }, // Este
			{ -1, 0 }, // Oeste
	};

	private Square startingPoint;
	private Square nextPoint;
	private int idx = 0;

	public ReySquareIterator(Square startingPoint) {
		this.startingPoint = startingPoint;
		calcularNextPoint();
	}

	private void calcularNextPoint() {
		this.nextPoint = null;
		while (this.idx < 8 && nextPoint == null) {
			this.nextPoint = Square.getSquare(startingPoint.getFile() + vector[idx][0],
					startingPoint.getRank() + vector[idx][1]);
			this.idx++;
		}
		;
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

}
