package iterators;

import chess.Square;

public class CardinalSquareIterator implements SquareIterator {
	
	public enum Cardinal{
		Norte, Sur, Este, Oeste
	}
	
	private Square nextPoint;
	private Cardinal cardinal;
	
	public CardinalSquareIterator(Cardinal cardinal, Square startingPoint) {
		this.cardinal = cardinal;
		calcularNextPoint(startingPoint);
	}

	private void calcularNextPoint(Square from) {
		switch (cardinal) {
		case Norte:
			this.nextPoint =  Square.getSquare(from.getFile(), from.getRank() + 1);
			break;
		case Sur:
			this.nextPoint =  Square.getSquare(from.getFile(), from.getRank() - 1);	
			break;
		case Este:
			this.nextPoint =  Square.getSquare(from.getFile() + 1, from.getRank());	
			break;			
		case Oeste:
			this.nextPoint =  Square.getSquare(from.getFile() - 1, from.getRank());	
			break;
		default:
			throw new RuntimeException("Cardinal not defined");
		}
	}

	@Override
	public boolean hasNext() {
		return nextPoint !=null;
	}

	@Override
	public Square next() {
		Square currentPoint = nextPoint;
		calcularNextPoint(currentPoint);
		return currentPoint;
	}

}
