package chess;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import iterators.BoardIterator;
import iterators.BottomUpSquareIterator;
import iterators.SquareIterator;

public class DummyBoard implements Iterable<Map.Entry<Square, Pieza>>{
	//56,57,58,59,60,61,62,63,
	//48,49,50,51,52,53,54,55,
	//40,41,42,43,44,45,46,47,
	//32,33,34,35,36,37,38,39,
	//24,25,26,27,28,29,30,31,
	//16,17,18,19,20,21,22,23,
    //08,09,10,11,12,13,14,15,
    //00,01,02,03,04,05,06,07,	
	private Pieza[][] tablero;
	
	public DummyBoard(Pieza[][] tablero){
		this.tablero = tablero;
	}

	public DummyBoard(DummyBoard tablero) {
		// TODO Auto-generated constructor stub
	}

	public Pieza getPieza(Square square) {
		return tablero[square.getFile()][square.getRank()];
	}

	public boolean isEmtpy(Square square) {
		return getPieza(square) == null;
	}
	
	public BoardIterator iterator(SquareIterator squareIterator){
		return new BoardIterator(){
			@Override
			public boolean hasNext() {
				return squareIterator.hasNext();
			}
			
			@Override
			public SimpleImmutableEntry<Square, Pieza> next() {
				Square currentSquare = squareIterator.next();
				Pieza pieza = getPieza(currentSquare);
				return new SimpleImmutableEntry<Square, Pieza>(currentSquare, pieza);
			}
		};
	}

	@Override
	public Iterator<Map.Entry<Square, Pieza>> iterator() {
		return iterator(new BottomUpSquareIterator());
	}

	public void move(Move move) {
		// TODO Auto-generated method stub
		
	}

	private Square getKingSquare(Color color) {
		Square kingSquare = null;
		for (Map.Entry<Square, Pieza> entry : this) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(currentPieza != null){
				if(currentPieza.isRey() && color.equals(currentPieza.getColor())){
					kingSquare = currentSquare;
					break;
				}
			}			
		}
		return kingSquare;
	}
	
	public boolean isKingInCheck(Color color) {
		Square kingSquare = getKingSquare(color.opositeColor());
		for (Map.Entry<Square, Pieza> entry : this) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor().opositeColor())){
					if(currentPieza.puedeCapturarRey(this, currentSquare, kingSquare)){
						return true;
					}
				}
			}			
		}
		return false;
	}

}
