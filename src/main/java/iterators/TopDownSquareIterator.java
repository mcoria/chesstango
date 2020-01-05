package iterators;

import chess.Square;

public class TopDownSquareIterator implements SquareIterator {

	private Square nextSquare = Square.a8;
	
	@Override
	public boolean hasNext() {
		return this.nextSquare != null;
	}

	@Override
	public Square next() {
		Square currentSquare = this.nextSquare;
		
		int file = this.nextSquare.getFile();
		int rank = this.nextSquare.getRank();
				
		if(file < 8){
			file++;
		}
		
		if(file == 8){
			file = 0;
			rank--;
		}
		
		if(rank >=0 ){
			this.nextSquare = Square.getSquare(file, rank);
		} else {
			this.nextSquare = null;
		}
		
		return currentSquare;
	}
}
