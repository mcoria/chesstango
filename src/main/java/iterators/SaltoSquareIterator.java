package iterators;

import chess.Square;

public class SaltoSquareIterator implements SquareIterator {

	public final static int[][] SALTOS_REY = { { 0, 1 }, // Norte
			{ 1, 1 }, // NE
			{ -1, 1 }, // NO
			{ 0, -1 }, // Sur
			{ 1, -1 }, // SE
			{ -1, -1 }, // SO
			{ 1, 0 }, // Este
			{ -1, 0 }, // Oeste
	};
	
	public final static int[][] SALTOS_CABALLO = { 
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
	
	private int[][] saltos;

	private Square startingPoint;
	private Square nextPoint;
	private int idx = 0;

	public SaltoSquareIterator(Square startingPoint, int[][] saltos) {
		this.startingPoint = startingPoint;
		this.saltos = saltos;
		calcularNextPoint();
	}

	private void calcularNextPoint() {
		this.nextPoint = null;
		while (this.idx < saltos.length && nextPoint == null) {
			this.nextPoint = Square.getSquare(startingPoint.getFile() + saltos[idx][0],
					startingPoint.getRank() + saltos[idx][1]);
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
